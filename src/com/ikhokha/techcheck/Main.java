package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		
		Map<String, Integer> totalResults = new HashMap<>();
				
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));

		assert commentFiles != null;
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);
		for (File commentFile : commentFiles) {
			executor.submit(() -> {
				CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
				Map<String, Integer> fileResults = commentAnalyzer.analyze();
				addReportResults(fileResults, totalResults);
			});
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			System.out.println("RESULTS\n=======");
			totalResults.forEach((k,v) -> System.out.println(k + " : " + v));
		} catch (InterruptedException e) {
			System.out.println("Error: "+e);
		}
	}
	
	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {

		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			target.merge(entry.getKey(), entry.getValue(), Integer::sum);
		}
		
	}

}
