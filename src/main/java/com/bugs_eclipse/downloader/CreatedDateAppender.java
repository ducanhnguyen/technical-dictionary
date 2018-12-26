package com.bugs_eclipse.downloader;

import java.io.File;
import java.util.List;

import com.bugs_eclipse.utils.MyUtils;

public class CreatedDateAppender {
	// contain a list of csv files
	private File csvFolderInput;
	
	// Merge all csv files into a unique file
	private File csvFileOutput;

	public CreatedDateAppender() {
	}

	public static void main(String[] args) {
		CreatedDateAppender appender = new CreatedDateAppender();
		appender.setCsvFolderInput(new File("C:\\Users\\adn0019\\Desktop\\jdt_month"));
		appender.setCsvFileOutput(new File("C:\\Users\\adn0019\\Desktop\\jdt_month_full.csv"));
		appender.appendCreatedDate();
	}

	public void appendCreatedDate() {
		String header = "\"Bug ID\",\"Product\",\"Component\",\"Assignee\",\"Status\",\"Resolution\",\"Summary\",\"Changed\",\"Created_year\",\"Created_month\"";
		String fullDataInStr = header;
		fullDataInStr += "\n";

		// Merge all data
		File[] listOfFiles = csvFolderInput.listFiles();

		for (int i = 0; i < csvFolderInput.listFiles().length; i++) {
			System.out.println(1.0f * i / csvFolderInput.listFiles().length);
			if (listOfFiles[i].isFile()) {
				// Step 1: name file = "{year}-{month}.csv"
				String nameFile = listOfFiles[i].getName().replace(".csv", "");
				String createdYear = nameFile.split("-")[0];
				String createdMonth = nameFile.split("-")[1];

				// Step 2
				List<String> csvContent = MyUtils.readFileContent(listOfFiles[i]);
				String newFileContent = "";
				// ignore header
				for (int j = 1; j <= csvContent.size() - 1; j++) {
					String newLine = csvContent.get(j) + "," + createdYear + "," + createdMonth;
					newFileContent += newLine + "\n";
				}

				fullDataInStr += newFileContent;
			} else if (listOfFiles[i].isDirectory()) {
				// nothing to do
			}
		}
		// write to file
		System.out.println("Exporting");
		MyUtils.exportToFile(csvFileOutput, fullDataInStr);
	}

	public void setCsvFolderInput(File csvFolder) {
		this.csvFolderInput = csvFolder;
	}

	public File getCsvFolderInput() {
		return csvFolderInput;
	}

	public File getCsvFileOutput() {
		return csvFileOutput;
	}

	public void setCsvFileOutput(File csvFolderOutput) {
		this.csvFileOutput = csvFolderOutput;
	}
}
