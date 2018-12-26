package com.bugs_eclipse.downloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.bugs_eclipse.IConfiguration;
import com.bugs_eclipse.utils.MyUtils;

/**
 * Download all bug report of a product in bugzilla. There is no comment in
 * these bug reports.
 * 
 * @author adn0019
 *
 */
public class BugReportDownloader {
	public static void main(String[] args) {
		List<Object[]> sources = new ArrayList<Object[]>();
		sources.add(new Object[] { "BIRT", "BIRT", 2005, 2018 }); // >=2000 bugs

//		sources.add(new Object[] { "ECD", "CFT", 2015, 2018 }); // 199 bugs
//		sources.add(new Object[] { "ECD", "Dirigible", 2015, 2018 }); // 346 bugs
		sources.add(new Object[] { "ECD", "Orion", 2011, 2018 }); // >= 2000 bugs

		sources.add(new Object[] { "IoT", "4DIAC", 2015, 2018 });// 899 bugs
//		sources.add(new Object[] { "IoT", "Mihini", 2013, 2018 });// 121 bugs
//		sources.add(new Object[] { "IoT", "OM2M", 2014, 2018 }); // 70 bugs
//		sources.add(new Object[] { "IoT", "Paho", 2012, 2018 }); // 109 bugs
//		sources.add(new Object[] { "IoT", "Scada", 2013, 2018 }); // 104 bugs
//		sources.add(new Object[] { "IoT", "Vorto", 2014, 2018 }); // 159 bugs

//		sources.add(new Object[] { "LocationTech", "JTS", 2014, 2018 }); // 91 bugs

		sources.add(new Object[] { "Eclipse", "e4", 2008, 2018 }); // > 2000 bugs
		sources.add(new Object[] { "Eclipse", "Equinox", 2003, 2018 }); // > 2000 bugs
		sources.add(new Object[] { "Eclipse", "JDT", 2001, 2018 }); // > 2000 bugs
		sources.add(new Object[] { "Eclipse", "PDE", 2001, 2018 }); // > 2000 bugs
		sources.add(new Object[] { "Eclipse", "Platform", 2001, 2018 });// > 2000 bugs

		sources.add(new Object[] { "EE4J", "EclipseLink", 2008, 2018 });// > 2000 bugs

		sources.add(new Object[] { "Eclipse%20Foundation", "Community", 2004, 2018 });// > 2000 bugs
//		sources.add(new Object[] { "Eclipse%20Foundation", "Working%20Groups", 2009, 2018 }); // 283 bugs
		sources.add(new Object[] { "Eclipse%20Foundation", "z_Archived", 2004, 2018 });// > 2000 bugs

		sources.add(new Object[] { "Modeling", "Acceleo", 2009, 2018 }); // 957 bugs
//		sources.add(new Object[] { "Modeling", "Amalgam", 2009, 2018 }); // 145 bugs
//		sources.add(new Object[] { "Modeling", "AMP", 2009, 2018 });// 174 bugs
//		sources.add(new Object[] { "Modeling", "Ecoretools ", 2008, 2018 }); // 316 bugs
		sources.add(new Object[] { "Modeling", "ECP", 2011, 2018 }); // > 2000 bugs
//		sources.add(new Object[] { "Modeling", "Edapt", 2011, 2018 });// 152 bugs
//		sources.add(new Object[] { "Modeling", "EEF", 2009, 2018 }); // 366 bugs
		sources.add(new Object[] { "Modeling", "EMF", 2002, 2018 }); // >= 2000 bugs
//		sources.add(new Object[] { "Modeling", "EMF%20Services", 2006, 2018 }); // 468 bugs
//		sources.add(new Object[] { "Modeling", "EMF.Diffmerge", 2012, 2018 });// 268 bugs
//		sources.add(new Object[] { "Modeling", "EMF.EGF", 2011, 2018 });// 206 bugs
//		sources.add(new Object[] { "Modeling", "EMF.Parsley", 2013, 2018 });// 173 bugs
		sources.add(new Object[] { "Modeling", "EMFCompare", 2008, 2018 });// 1263 bugs
		sources.add(new Object[] { "Modeling", "EMFStore", 2011, 2018 });// 549 bugs
		sources.add(new Object[] { "Modeling", "EMFT", 2006, 2018 });// 633 bugs
		sources.add(new Object[] { "Modeling", "EMFT.facet", 2010, 2018 });
		sources.add(new Object[] { "Modeling", "EMFT.Henshin", 2010, 2018 });
		sources.add(new Object[] { "Modeling", "Epsilon", 2008, 2018 }); // 603 bugs
		sources.add(new Object[] { "Modeling", "eTrice", 2011, 2018 });// 630 bugs
		sources.add(new Object[] { "Modeling", "GMF-Runtime", 2005, 2018 });// > 2000 bugs
		sources.add(new Object[] { "Modeling", "GMF-Tooling", 2005, 2018 });// > 2000 bugs
//		sources.add(new Object[] { "Modeling", "GMP", 2010, 2018 }); // 246 bugs
//		sources.add(new Object[] { "Modeling", "Graphiti ", 2010, 2018 }); // 621 bugs
		sources.add(new Object[] { "Modeling", "M2T", 2005, 2018 }); // 1075 bugs
		sources.add(new Object[] { "Modeling", "MDT.MoDisco", 2010, 2018 });// 1031 bugs
//		sources.add(new Object[] { "Modeling", "MDT.RMF", 2012, 2018 }); // 350 bugs
		sources.add(new Object[] { "Modeling", "MDT.UML2", 2004, 2018 }); // 1042 bugs
//		sources.add(new Object[] { "Modeling", "MDT.XSD", 2002, 2018 }); // 409 bugs
//		sources.add(new Object[] { "Modeling", "MMT.ATL", 2007, 2018 }); // 310 bugs
//		sources.add(new Object[] { "Modeling", "Modeling", 2006, 2018 }); // 227 bugs
		sources.add(new Object[] { "Modeling", "OCL", 2006, 2018 }); // > 2000 bugs
		sources.add(new Object[] { "Modeling", "Papyrus", 2008, 2018 });// > 2000 bugs
		sources.add(new Object[] { "Modeling", "Papyrus-rt", 2015, 2018 });// 1047 bugs
//		sources.add(new Object[] { "Modeling", "QVTd", 2015, 2018 });// 361 bugs
		sources.add(new Object[] { "Modeling", "QVTo", 2007, 2018 }); // 1042 bugs
		sources.add(new Object[] { "Modeling", "Sirius", 2013, 2018 });// >2000 bugs
//		sources.add(new Object[] { "Modeling", "Sphinx", 2011, 2018 });// 482 bugs
		sources.add(new Object[] { "Modeling", "TMF", 2008, 2018 }); // > 2000 bugs
		sources.add(new Object[] { "Modeling", "Viatra", 2012, 2018 }); // 1300 bugs

		sources.add(new Object[] { "Mylyn", "Mylyn", 2005, 2018 }); // > 2000 bugs
//		sources.add(new Object[] { "Mylyn", "Mylyn%20Builds", 2010, 2018 }); // 290 bugs
//		sources.add(new Object[] { "Mylyn", "Mylyn%20Commons", 2010, 2018 });// 205 bugs
//		sources.add(new Object[] { "Mylyn", "Mylyn%20Context", 2010, 2018 }); // 258 bugs
//		sources.add(new Object[] { "Mylyn", "Mylyn%20Context%20MFT", 2011, 2018 });// 120 bugs
		sources.add(new Object[] { "Mylyn", "Mylyn%20Docs", 2008, 2018 }); // 1000 bugs
		sources.add(new Object[] { "Mylyn", "Mylyn%20Reviews", 2010, 2018 });// 679 bugs
		sources.add(new Object[] { "Mylyn", "Mylyn%20Reviews%20R4E", 2011, 2018 });// 716 bugs
		sources.add(new Object[] { "Mylyn", "Mylyn%20Tasks", 2010, 2018 });// 1300 bugs

		sources.add(new Object[] { "RT", "ECF", 2004, 2018 });// > 2000 bugs
		sources.add(new Object[] { "RT", "ERCP", 2004, 2018 });// 600 bugs
//		sources.add(new Object[] { "RT", "Gemini.Web", 2010, 2018 }); // 226 bugs
		sources.add(new Object[] { "RT", "Jetty", 2009, 2018 });// > 2000 bugs
		sources.add(new Object[] { "RT", "RAP", 2006, 2018 });// > 2000 bugs
		sources.add(new Object[] { "RT", "Riena", 2008, 2018 }); // 1014 bugs
//		sources.add(new Object[] { "RT", "Smila", 2008, 2018 });// 221 bugs
//		sources.add(new Object[] { "RT", "Vertx", 2013, 2018 }); // 398 bugs
		sources.add(new Object[] { "RT", "Virgo", 2010, 2018 });// 1672 bugs

		sources.add(new Object[] { "SOA", "BPEL", 2006, 2018 }); // 385 bugs
		sources.add(new Object[] { "SOA", "BPMN2Modeler", 2012, 2018 }); // 770 bugs
		sources.add(new Object[] { "SOA", "JWT", 2008, 2018 }); // 411 bugs

		sources.add(new Object[] { "Technology ", "ACTF", 2013, 2018 }); // 1000 bugs
		sources.add(new Object[] { "Technology ", "Babel", 2008, 2018 });// 700 bugs
		sources.add(new Object[] { "Technology ", "CBI", 2012, 2018 });// 700 bugs
		sources.add(new Object[] { "Technology ", "DLTK", 2007, 2018 });// 1200 bugs
		sources.add(new Object[] { "Technology", "Ease", 2014, 2018 });// 700 bugs
		sources.add(new Object[] { "Technology", "Efxclipse", 2013, 2018 }); // 1100 bugs
		sources.add(new Object[] { "Technology", "EGit", 2009, 2018 }); // > 2000 bugs
		sources.add(new Object[] { "Technology", "EPF", 2006, 2018 }); // > 2000 bugs
		sources.add(new Object[] { "Technology", "EPP", 2007, 2018 });// 1800 bugs
		sources.add(new Object[] { "Technology", "Hudson", 2011, 2018 });// 1400 bugs
		sources.add(new Object[] { "Technology", "JGit", 2009, 2018 });// 1200 bugs
		sources.add(new Object[] { "Technology", "Jubula", 2010, 2018 }); // >2000 bugs
		sources.add(new Object[] { "Technology", "Lyo", 2011, 2018 });// 1200 bugs
		sources.add(new Object[] { "Technology", "m2e", 2008, 2018 });// 1800 bugs
		sources.add(new Object[] { "Technology", "MPC", 2010, 2018 }); // 700 bugs
		sources.add(new Object[] { "Technology", "NatTable", 2012, 2018 }); // 912 bugs
		sources.add(new Object[] { "Technology", "Nebula", 2006, 2018 });// 1400 bugs
		sources.add(new Object[] { "Technology", "Recommenders", 2011, 2018 });// 1300 bugs
		sources.add(new Object[] { "Technology", "RTSC", 2009, 2018 });// 1250 bugs
		sources.add(new Object[] { "Technology", "Sapphire", 2010, 2018 });// 1200 bugs
		sources.add(new Object[] { "Technology", "Scout", 2010, 2018 }); // 2000 bugs
		sources.add(new Object[] { "Technology", "Subversive", 2007, 2018 }); // >2000 bugs
		sources.add(new Object[] { "Technology", "SWTBot", 2008, 2018 }); // 600 bugs
		sources.add(new Object[] { "Technology", "Tigerstripe", 2008, 2018 });// 1200 bugs
		sources.add(new Object[] { "Technology", "Tycho", 2011, 2018 });// 1200 bugs

		sources.add(new Object[] { "Tool", "AJDT", 2002, 2018 });// 1500 bugs
		sources.add(new Object[] { "Tool", "AspectJ", 2003, 2018 }); // >2000 bugs
		sources.add(new Object[] { "Tool", "Buckminster", 2005, 2018 });// 1200 bugs
		sources.add(new Object[] { "Tool", "CDT", 2002, 2018 });// >2000 bugs
		sources.add(new Object[] { "Tool", "Data%20Tools", 2005, 2018 }); // >2000 bugs
		sources.add(new Object[] { "Tool", "GEF", 2002, 2018 });// >2000 bugs
		sources.add(new Object[] { "Tool", "Linux%20Tools", 2007, 2018 });// >2000 bugs
		sources.add(new Object[] { "Tool", "MTJ", 2006, 2018 });// 800 bugs
		sources.add(new Object[] { "Tool", "Objectteams ", 2010, 2018 });// 800 bugs
		sources.add(new Object[] { "Tool", "Oomph", 2014, 2018 }); // 1700 bugs
		sources.add(new Object[] { "Tool", "Orbit", 2006, 2018 });// 1200 bugs
		sources.add(new Object[] { "Tool", "PDT", 2006, 2018 }); // >2000 bugs
		sources.add(new Object[] { "Tool", "PTP", 2005, 2018 });// >2000 bugs
		sources.add(new Object[] { "Tool", "Target%20Management", 2006, 2018 }); // >2000 bugs
		sources.add(new Object[] { "Tool", "TCF", 2011, 2018 });// 1200 bugs
		sources.add(new Object[] { "Tool", "Tracecompass", 2014, 2018 });// 700 bugs
		sources.add(new Object[] { "Tool", "WindowBuilder", 2011, 2018 });// 600 bugs
		sources.add(new Object[] { "Tool", "Xtend", 2011, 2018 }); // >600 bugs

		// add more later: WebTools, Science

		for (Object[] source : sources) {
			// Generate download links
			BugReportLinksGeneration linkDownloader = new BugReportLinksGeneration();
			linkDownloader.setClassification((String) source[0]);
			linkDownloader.setProduct((String) source[1]);
			linkDownloader.setStartingYear((Integer) source[2]);
			linkDownloader.setEndYear((Integer) source[3]);
			linkDownloader.generateLinks();
			Map<String, String> downloadLinks = linkDownloader.getDownloadLinks();

			// download
			BugReportDownloader downloader = new BugReportDownloader();

			File downloadFolder = new File(IConfiguration.RAW_DATA_FOLDER.getAbsolutePath()
					+ linkDownloader.getClassification() + File.separator + linkDownloader.getProduct());
			if (!downloadFolder.exists())
				downloadFolder.mkdirs();

			for (String downloadFileName : downloadLinks.keySet()) {
				String downloadLink = downloadLinks.get(downloadFileName);
				// Opening firefox too many times to download sometimes causes unexpected
				// behaviours
				boolean success = false;
				do
					try {
						downloader.download(downloadFolder, downloadLink, downloadFileName);
						success = true;
					} catch (Exception e) {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				while (!success);
			}
		}
	}

	public void download(final File downloadFolder, final String downloadLink, final String downloadFileName) {
		// Check whether we downloaded it
		File downloadFile = new File(downloadFolder.getAbsolutePath() + File.separator + downloadFileName);
		if (downloadFile.exists()) {
			System.out.println(downloadFile.getAbsolutePath() + " exists! Ignore.");
		} else {
			// if you didn't update the Path system variable to add the full directory path
			// to the executable as above mentioned then doing this directly through code
			System.setProperty("webdriver.gecko.driver", GEKO_DRIVER);

			// Create FireFox Profile object
			FirefoxProfile profile = new FirefoxProfile();

			// Set Location to store files after downloading
			profile.setPreference("browser.download.dir", downloadFolder.getAbsolutePath());
			profile.setPreference("browser.download.folderList", 2);

			// Set Preference to not show file download confirmation dialogue using MIME
			// types Of different file extension types.
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			profile.setPreference("pdfjs.disabled", true);

			// Now you can Initialize marionette driver to launch firefox
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("marionette", true);

			// Pass FProfile parameter In webdriver to use preferences to download file.
			FirefoxOptions options = new FirefoxOptions().setProfile(profile);
			final WebDriver driver = new FirefoxDriver(options);

			// Open APP to download applications
			ExecutorService executor = Executors.newFixedThreadPool(4);
			Future<?> future = executor.submit(new Runnable() {
				public void run() {
					driver.navigate().to(downloadLink);
				}
			});
			executor.shutdown();
			try {
				System.out.println(downloadLink);
				future.get(3, TimeUnit.SECONDS);
			} catch (Exception e) {
				System.out.println(""); // it is an exception. but the file is still downloaded
			}

			// The name of download file is different from what we want.
			// we need to rename the download file
			renameDownloadFile(downloadFileName, downloadFolder);

			// close firefox
			try {
				Runtime.getRuntime().exec("taskkill /IM firefox.exe /F");
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void renameDownloadFile(final String downloadFileName, final File downloadFolder) {
		for (File f : downloadFolder.listFiles())
			// by default, the download file's name from bugzilla starts with "bugs-"
			if (f.getName().startsWith("bugs-")) {
				String oldFilePath = downloadFolder.getAbsolutePath() + File.separator + f.getName();
				String newFilePath = downloadFolder.getAbsolutePath() + File.separator + downloadFileName;
				MyUtils.renameFile(oldFilePath, newFilePath);
				break;
			}
	}

	// need to run Selenium in firefox
	// See:
	// https://firefox-source-docs.mozilla.org/testing/geckodriver/geckodriver/Support.html
	final String GEKO_DRIVER = "C:/Users/adn0019/WORK/workspace/eclipse/bugzilla_downloader/lib/selenium/geckodriver-v0.21.0-win64.exe";
}