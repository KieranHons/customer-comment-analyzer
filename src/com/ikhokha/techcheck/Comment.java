package com.ikhokha.techcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


interface StringFunction {
    boolean func(String param);
}
public class Comment {
    Map<String, StringFunction> metrics = new HashMap<>();

    Comment() {
        this.addMetric("SHORTER_THAN_15",s -> s.length() < 15);
        this.addMetric("MOVER_MENTIONS",s -> s.contains("mover"));
        this.addMetric("SHAKER_MENTIONS",s -> s.contains("shaker"));
        this.addMetric("QUESTIONS",s -> s.contains("?"));
        this.addMetric("SPAM",s -> {
            String http = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
            Pattern pattern = Pattern.compile(http);
            Matcher matcher = pattern.matcher(s);
            return matcher.find();
        });
    }

    private boolean takingMethod(StringFunction sf, String s) {
        return sf.func(s);
    }

    void addMetric(String metric, StringFunction sf) {
        this.metrics.put(metric, sf);
    }

    List<String> checkLine(String line) {
        List<String> passed = new ArrayList<>();
        metrics.forEach((k,v) -> {
            if (this.takingMethod(v,line)) passed.add(k);
        });
        return passed;
    }
}
