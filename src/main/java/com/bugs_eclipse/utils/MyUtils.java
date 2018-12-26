package com.bugs_eclipse.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class MyUtils {
	public static boolean exportToFile(File file, String content) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<String> readFileContent(File file) {
		List<String> content = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null)
				content.add(str);
			in.close();
		} catch (IOException e) {
		}
		return content;
	}

	// Get the number of days in that month
	public static int getNumofDayInAMonth(int year, int month) {
		YearMonth yearMonthObject = YearMonth.of(year, month);
		int daysInMonth = yearMonthObject.lengthOfMonth();
		return daysInMonth;
	}

	public static void renameFile(String oldFilePath, String newFilePath) {
		// File (or directory) with old name
		File file = new File(oldFilePath);

		// File (or directory) with new name
		File file2 = new File(newFilePath);

		if (file2.exists()) {
			System.out.println("File is existed!");
		} else {
			// Rename file (or directory)
			boolean success = file.renameTo(file2);

			if (!success) {
				// File was not successfully renamed
			}
		}
	}

	public static List<File> getAllFiles(File folder) {
		List<File> files = new ArrayList<>();
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files.add(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				files.addAll(getAllFiles(listOfFiles[i]));
			}
		}
		return files;
	}
}
