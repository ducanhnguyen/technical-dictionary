package com.bugs_eclipse.downloader;

import java.util.List;

import javax.swing.SwingWorker;

import example.CommentsDownloader;

public class MySwingWorker extends SwingWorker<String, String> {
	public final String BASE_LINK = "https://bugs.eclipse.org/bugs/show_bug.cgi?id=";

	private String swingWorkerName;
	private int startIndexOfBugReport;
	private int endIndexOfBugReport;
	private List<String> csvContent;

	private int bugReportIdIndex;

	private List<String> bugReportsOutput;

	public MySwingWorker(String swingWorkerName, int startIndexOfBugReport, int endIndexOfBugReport,
			List<String> csvContent, int bugReportIdIndex, List<String> bugReportsOutput) {
		this.swingWorkerName = swingWorkerName;
		this.startIndexOfBugReport = startIndexOfBugReport;
		this.endIndexOfBugReport = endIndexOfBugReport;
		this.csvContent = csvContent;
		this.bugReportIdIndex = bugReportIdIndex;
		this.bugReportsOutput = bugReportsOutput;

		System.out.println("\tCreate a swing worker from " + startIndexOfBugReport + " to " + endIndexOfBugReport);
	}

	@Override
	protected String doInBackground() throws Exception {
		String newCsvContent = "";
		int numAnalyzedReports = 0;

		for (int index = startIndexOfBugReport; index <= endIndexOfBugReport; index++)
			// check bound
			if (endIndexOfBugReport >= 0 && endIndexOfBugReport < csvContent.size()) {
				// only for testing - begin
				numAnalyzedReports++;
				if (numAnalyzedReports % 10 == 0)
					System.out.println("\tanalyzed " + numAnalyzedReports + " bug reports in " + swingWorkerName
							+ ". Remaining bug reports = "
							+ (endIndexOfBugReport - startIndexOfBugReport + 1 - numAnalyzedReports));
				// only for testing - end

				String bugReportID = csvContent.get(index).split(",")[bugReportIdIndex];
				String bugReportLink = BASE_LINK + bugReportID;

				// Download comments from bugzilla
				CommentsDownloader commentsExtracter = new CommentsDownloader();
				commentsExtracter.setBugReportLink(bugReportLink);
				commentsExtracter.extractComments();
				List<String> comments = commentsExtracter.getComments();

				String allComments = "";
				for (String comment : comments) {
					allComments += normalizeComment(comment) + ".";
				}

				newCsvContent += csvContent.get(index) + "," + putInMarks(allComments);
				newCsvContent += "\n";
			}
		publish(newCsvContent);

		return newCsvContent;
	}

	@Override
	protected void process(List<String> arg0) {
		bugReportsOutput.addAll(arg0);
	}

	@Override
	protected void done() {
		super.done();
	}

	private String putInMarks(String str) {
		return "\"" + str + "\"";
	}

	private String normalizeComment(String comment) {
		comment = comment.trim();
		comment = comment.replace("\n", " ");
		comment = comment.replace("\r", " ");
		comment = comment.replace("\r\n", " ");
		comment = comment.replace("\n\r", " ");
		comment = comment.replace("\"", "'");
		comment = comment.replace(",", ";");
		comment = comment.replaceAll("\\s+", " ");
		return comment;
	}

	public void setStartIndexOfBugReport(int startIndex) {
		this.startIndexOfBugReport = startIndex;
	}

	public void setEndIndexOfBugReport(int endIndex) {
		this.endIndexOfBugReport = endIndex;
	}

	public List<String> getBugReportsOutput() {
		return bugReportsOutput;
	}

	public void setBugReportsOutput(List<String> output) {
		this.bugReportsOutput = output;
	}

	public void setCsvContent(List<String> csvContent) {
		this.csvContent = csvContent;
	}

	public List<String> getCsvContent() {
		return csvContent;
	}
}