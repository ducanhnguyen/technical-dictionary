package com.bugs_eclipse.statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bugs_eclipse.IConfiguration;
import com.bugs_eclipse.utils.MyUtils;

public class WordsStatisticsMerger {
	// Contain a set of csv files. Each csv file stores a list of words using in a
	// product.
	private File wordsStatisticsFolder = null;

	public WordsStatisticsMerger() {
	}

	public static void main(String[] args) {
		WordsStatisticsMerger wm = new WordsStatisticsMerger();
		wm.setWordsStatisticsFolder(IConfiguration.WORD_STATISTICS_FOLDER);
		System.out.println("merging");
		Words w = wm.merge();
		System.out.println(w.size() + " words");

		System.out.println("exporting to csv");
		wm.exportWordsToCsv(w, new File("C:\\Users\\adn0019\\Desktop\\words.csv"));
	}

	public Words merge() {
		Words w = new Words();

		if (getWordsStatisticsFolder() != null && getWordsStatisticsFolder().exists()) {

			for (File classification : getWordsStatisticsFolder().listFiles())
				if (classification.isDirectory())
					for (File product : classification.listFiles()) {

						List<String> words_statistics = MyUtils.readFileContent(product);

						for (int i = 1; i < words_statistics.size(); i++) {
							String word = words_statistics.get(i).split(",")[1];
							int occurence = Integer.parseInt(words_statistics.get(i).split(",")[2]);

							w.update(word, generateProductName(classification.getName(), product.getName()), occurence);
						}
					}
		}
		return w;
	}

	private String generateProductName(String classification, String product) {
//		return classification + "." + product.replace(".csv", "");
		return product.replace(".csv", "");
	}

	private List<String> getAllProductName(File wordsStatisticsFile) {
		List<String> productNames = new ArrayList<>();

		for (File classification : wordsStatisticsFile.listFiles())
			for (File product : classification.listFiles())
				productNames.add(generateProductName(classification.getName(), product.getName()));

		return productNames;
	}

	public void exportWordsToCsv(Words w, File csvFile) {
		if (w != null) {
			String csvContent = "";
			List<String> productNames = getAllProductName(this.wordsStatisticsFolder);

			// create header
			String header = "word,";
			header += "total,"; // the total number of occurrences
			for (String productName : productNames)
				header += productName + ",";
			header = header.substring(0, header.length() - 1);
			csvContent = header + "\n";

			// add statistics of all products
//			int count = 0;
			for (String word : w.keySet()) {

//				count++;
//				if (count == 1000)
//					break;

				WordStatistics ws = w.get(word);

				String newLine = "";

				int totalOfOccurrence = 0;

				for (String productName : productNames) {
					int occurrence = 0;
					if (ws.containsKey(productName))
						occurrence = ws.get(productName);
					totalOfOccurrence += occurrence;

					newLine += occurrence + ",";
				}
				newLine = word + "," + totalOfOccurrence + "," + newLine;
				newLine = newLine.substring(0, newLine.length() - 1);
				csvContent += newLine + "\n";
			}

			MyUtils.exportToFile(csvFile, csvContent);
			System.out.println("Export to " + csvFile.getAbsolutePath() + " done");
		}
	}

	public void setWordsStatisticsFolder(File wordsStatisticsFile) {
		this.wordsStatisticsFolder = wordsStatisticsFile;
	}

	public File getWordsStatisticsFolder() {
		return wordsStatisticsFolder;
	}

}
