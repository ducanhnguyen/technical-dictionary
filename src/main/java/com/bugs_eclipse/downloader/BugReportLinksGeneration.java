package com.bugs_eclipse.downloader;

import java.util.HashMap;
import java.util.Map;

import com.bugs_eclipse.utils.MyUtils;

/**
 * Generate download links in bugzilla. Categorize by month.
 *
 * Link: https://bugs.eclipse.org/bugs/buglist.cgi?quicksearch=e4
 */
public class BugReportLinksGeneration {
	private Map<String, String> downloadLinks = new HashMap<String, String>();
	private int startingYear = -1;
	private int endYear = -1;
	// Ex: BIRT, ECD, Eclipse, etc.
	// For more detail, see: https://bugs.eclipse.org/bugs/query.cgi
	private String classification;

	// Ex: in Eclipse, we have CDT, JDT, E4, etc.
	private String product;

	public static void main(String[] args) {
		BugReportLinksGeneration linkDownloader = new BugReportLinksGeneration();
		linkDownloader.setStartingYear(2008);
		linkDownloader.setEndYear(2018);
		linkDownloader.setClassification("Eclipse");
		linkDownloader.setProduct("Equinox");
		linkDownloader.generateLinks();

		System.out.println("Download links");
		for (String key : linkDownloader.getDownloadLinks().keySet()) {
			System.out.println(key);
			System.out.println(linkDownloader.getDownloadLinks().get(key));
			System.out.println();
		}
	}

	public BugReportLinksGeneration() {
	}

	public Map<String, String> generateLinks() {
		if (getClassification() != null && getProduct() != null) {

			for (int year = startingYear; year <= endYear; year++)

				for (int month = 1; month <= 12; month++) {

					int numOfDay = MyUtils.getNumofDayInAMonth(year, month);

					String downloadLink = "https://bugs.eclipse.org/bugs/buglist.cgi?chfield=%5BBug%20creation%5D&chfieldfrom="
							+ year + "-" + month + "-01&chfieldto=" + year + "-" + month + "-" + numOfDay
							+ "&classification=" + getClassification() + "&product=" + getProduct()
							+ "&query_format=advanced&ctype=csv&human=1";

					String downloadFileName = year + "-" + month + ".csv";

					downloadLinks.put(downloadFileName, downloadLink);
				}

		}
		return downloadLinks;

	}

	public void setStartingYear(int startingYear) {
		this.startingYear = startingYear;
	}

	public int getStartingYear() {
		return startingYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public Map<String, String> getDownloadLinks() {
		return downloadLinks;
	}

	public void setDownloadLinks(Map<String, String> downloadLinks) {
		this.downloadLinks = downloadLinks;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getClassification() {
		return classification;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProduct() {
		return product;
	}
}
