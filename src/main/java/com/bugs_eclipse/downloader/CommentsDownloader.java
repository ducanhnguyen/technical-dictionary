package com.bugs_eclipse.downloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bugs_eclipse.IConfiguration;
import com.bugs_eclipse.utils.MyUtils;

/**
 * Download comments and append it to bug reports on a csv file.
 * 
 * For every bug report, we need to crawl its comment on bugzilla. In this step,
 * we need to use swing worker to accelerate the speed of crawling.
 * 
 * @author adn0019
 *
 */
public class CommentsDownloader {
	public static final String COMMENTS_COLUMN_NAME = "\"Comments\"";
	public static final String BUG_ID_COLUMN_NAME = "\"Bug ID\"";
	public static final int MINUMUM_BUG_REPORTS_TO_START_SWING_WORKDER = 20;
	public static final int NUM_SWING_WORKERS = 20;

	// The original csv file where we will add comments
	private File csvFileInput;

	// The csv file after adding comments
	private File csvFileOutput;

	public CommentsDownloader() {
	}

	public static void main(String[] args) {
		File allCsvFolder = IConfiguration.RAW_DATA_FOLDER;
		List<File> allFiles = MyUtils.getAllFiles(allCsvFolder);

		// count the number of analyzed file
		int numOfAnalyzedFile = 0;
		for (File file : allFiles)
			// only consider the original bug reports downloaded from bugzilla
			if (!file.getName().startsWith("_"))
				numOfAnalyzedFile++;

		int count = 0;
		for (File file : allFiles)
			// only consider the original file downloaded from bugzilla
			if (!file.getName().startsWith("_")) {

				count++;
				System.out.println("\n\n[" + count + "/" + numOfAnalyzedFile + "] " + file.getAbsolutePath());

				CommentsDownloader appender = new CommentsDownloader();
				appender.setCsvFileInput(file);

				File csvOutputFile = new File(
						file.getParentFile().getAbsolutePath() + File.separator + "_" + file.getName());

				if (!csvOutputFile.exists()) {
					appender.setCsvFileOutput(csvOutputFile);
					appender.appendComments();

				} else {
					// check its content
					List<String> csvContent = MyUtils.readFileContent(csvOutputFile);
					if (csvContent.size() >= 2) {// have a header and at least a bug report
						System.out.println("Add comments before. Ignoring!");

					} else {
						System.out.println("Add again!");
						csvOutputFile.delete();
						appender.setCsvFileOutput(csvOutputFile);
						appender.appendComments();
					}
				}
			}
	}

	/**
	 * 
	 * 
	 * @param header Example of header: "Bug
	 *               ID","Product","Component","Assignee","Status","Resolution","Summary","Changed"
	 * @return
	 */
	private int findBugReportIdIndex(String[] header) {
		int bugReportIdIndex = -1;
		for (int i = 0; i < header.length; i++)
			if (header[i].equals(BUG_ID_COLUMN_NAME)) {
				bugReportIdIndex = i;
				break;
			}

		return bugReportIdIndex;
	}

	public void appendComments() {
		if (csvFileInput != null) {
			String newCsvContent = "";

			List<String> csvContent = MyUtils.readFileContent(csvFileInput);

			if (csvContent.size() >= 2) {// have a header and at least a bug report
				String newHeader = csvContent.get(0) + "," + COMMENTS_COLUMN_NAME;
				newCsvContent = newHeader + "\n";

				int bugReportIdIndex = findBugReportIdIndex(csvContent.get(0).split(","));

				System.out.println("\t" + (csvContent.size() - 1) + " bug reports");

				// create swing workers
				List<MySwingWorker> swingWorkers = new ArrayList<>();
				List<String> outputInThread = new ArrayList<String>();

				if (csvContent.size() - 1 >= MINUMUM_BUG_REPORTS_TO_START_SWING_WORKDER) {
					int partSize = Math.round(csvContent.size() / NUM_SWING_WORKERS);

					for (int i = 0; i <= NUM_SWING_WORKERS - 2; i++)
						swingWorkers.add(new MySwingWorker("MySwingWorker " + i, i * partSize + 1, (i + 1) * partSize,
								csvContent, bugReportIdIndex, outputInThread));

					swingWorkers.add(new MySwingWorker("MySwingWorker-last", (NUM_SWING_WORKERS - 1) * partSize + 1,
							csvContent.size() - 1, csvContent, bugReportIdIndex, outputInThread));

				} else {
					swingWorkers.add(new MySwingWorker("MySwingWorker 1", 1, csvContent.size() - 1, csvContent,
							bugReportIdIndex, outputInThread));
				}

				// start all
				for (MySwingWorker swingWorker : swingWorkers)
					swingWorker.execute();

				// wait to done all
				boolean doneAll = false;
				while (!doneAll) {
					doneAll = true;
					for (MySwingWorker swingWorker : swingWorkers)
						if (!swingWorker.isDone())
							doneAll = false;
					try {
						Thread.sleep(50);
						// System.out.println("\t[.]Waiting to done all swing workers");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// write to file
				if (doneAll) {
					System.out.println(
							"Done all! There are " + outputInThread.size() + " parts. Merging! Exporting to file!");
					for (String outputInThreadItem : outputInThread)
						newCsvContent += outputInThreadItem;

					MyUtils.exportToFile(csvFileOutput, newCsvContent);
				}
			}
		}
	}

	public void setCsvFileInput(File csvFolder) {
		this.csvFileInput = csvFolder;
	}

	public File getCsvFileInput() {
		return csvFileInput;
	}

	public File getCsvFileOutput() {
		return csvFileOutput;
	}

	public void setCsvFileOutput(File csvFolderOutput) {
		this.csvFileOutput = csvFolderOutput;
	}

}
