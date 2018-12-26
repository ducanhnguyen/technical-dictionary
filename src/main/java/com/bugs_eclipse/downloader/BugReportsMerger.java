package com.bugs_eclipse.downloader;

import java.io.File;
import java.util.List;

import com.bugs_eclipse.IConfiguration;
import com.bugs_eclipse.utils.MyUtils;

/**
 * Merge all bug reports in a product into a unique csv file
 * 
 * @author adn0019
 *
 */
public class BugReportsMerger {
	// include a list of csv files
	private File inputBugReportsFolder;
	private File outputBugReportsFile;

	public static void main(String[] args) {
		File technicalBugReportsFolder = IConfiguration.RAW_DATA_FOLDER;

		for (File classification : technicalBugReportsFolder.listFiles())
			if (classification.isDirectory()) {
				File outputFolder = new File(IConfiguration.MERGE_FOLDER
						+ File.separator + classification.getName());
				if (!outputFolder.exists())
					outputFolder.mkdirs();

				for (File product : classification.listFiles())
					if (product.isDirectory()) {

						BugReportsMerger merger = new BugReportsMerger();
						merger.setInputBugReportsFolder(product);

						merger.setOutputBugReportsFile(
								new File(outputFolder.getAbsolutePath() + File.separator + product.getName() + ".csv"));
						merger.merge();
						System.out.println("Merge " + product.getName() + " done!");
					}
			}
	}

	public BugReportsMerger() {
	}

	public void merge() {
		if (inputBugReportsFolder != null && inputBugReportsFolder.exists() && inputBugReportsFolder.isDirectory()) {
			String allBugReports = "";

			for (File file : inputBugReportsFolder.listFiles())
				// only consider the bug report file adding comments
				if (file.getName().startsWith("_")) {
					System.out.println("Analyze " + file.getAbsolutePath());
					List<String> content = MyUtils.readFileContent(file);

					// Step 1: name file = "_{year}-{month}.csv"
					String createdYear = file.getName().split("-")[0].substring(1);
					String createdMonth = file.getName().split("-")[1].replace(".csv", "");

					// add header
					if (allBugReports.length() == 0)
						allBugReports = content.get(0) + "," + putInMarks("Created_year") + ","
								+ putInMarks("Created_month") + "\n";

					// add bug reports
					String contentStr = "";
					if (content.size() >= 2) {
						for (int i = 1; i < content.size(); i++)
							contentStr += content.get(i) + "," + putInMarks(createdYear) + ","
									+ putInMarks(createdMonth) + "\n";
					}

					allBugReports += contentStr;
				}

			MyUtils.exportToFile(outputBugReportsFile, allBugReports);
		}
	}

	private String putInMarks(String str) {
		return "\"" + str + "\"";
	}

	public void setInputBugReportsFolder(File inputBugReportsFolder) {
		this.inputBugReportsFolder = inputBugReportsFolder;
	}

	public File getInputBugReportsFolder() {
		return inputBugReportsFolder;
	}

	public void setOutputBugReportsFile(File outputBugReportsFolder) {
		this.outputBugReportsFile = outputBugReportsFolder;
	}

	public File getOutputBugReportsFile() {
		return outputBugReportsFile;
	}
}
