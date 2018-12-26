package example;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDownloader {
	private String downloadLink = null;
	private File downloadFolder = null;

	// need to run Selenium in firefox
	private File gekoDriver = null;

	public static void main(String[] args) {
		FirefoxDownloader downloader = new FirefoxDownloader();
		downloader.setDownloadLink(
				"https://bugs.eclipse.org/bugs/buglist.cgi?chfield=%5BBug%20creation%5D&chfieldfrom=2009-6-01&chfieldto=2009-6-30&classification=Eclipse&product=e4&query_format=advanced&ctype=csv&human=1");
		downloader.setDownloadFolder(new File("C:\\Users\\adn0019\\Downloads"));
		downloader.setGekoDriver(
				new File("C:/Users/adn0019/WORK/workspace/eclipse/bugzilla_downloader/lib/selenium/geckodriver-v0.21.0-win64.exe"));
		downloader.downloadLink();
		System.out.println("Download complete");
	}

	public void downloadLink() {
		if (downloadFolder != null && downloadLink != null && gekoDriver != null) {
			// if you didn't update the Path system variable to add the full directory path
			// to the executable as above mentioned then doing this directly through code
			System.setProperty("webdriver.gecko.driver", gekoDriver.getAbsolutePath());

			// Create FireFox Profile object
			FirefoxProfile profile = new FirefoxProfile();

			// Set Location to store files after downloading
			profile.setPreference("browser.download.dir", downloadFolder.getAbsolutePath());
			profile.setPreference("browser.download.folderList", 2);

			// Set Preference to not show file download confirmation dialogue using MIME
			// types Of different file extension types.
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv; charset=UTF-8");
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			profile.setPreference("pdfjs.disabled", true);

			// Now you can Initialize marionette driver to launch firefox
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("marionette", true);

			// Pass FProfile parameter In webdriver to use preferences to download file.
			FirefoxOptions options = new FirefoxOptions().setProfile(profile);
			WebDriver driver = new FirefoxDriver(options);

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
				// there is no download file
				System.out.println("Error occured! Ignore!");
			}

			// close firefox
			try {
				Runtime.getRuntime().exec("taskkill /IM firefox.exe /F");
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadFolder(File downloadFolder) {
		this.downloadFolder = downloadFolder;
	}

	public File getDownloadFolder() {
		return downloadFolder;
	}

	public void setGekoDriver(File gekoDriver) {
		this.gekoDriver = gekoDriver;
	}

	public File getGekoDriver() {
		return gekoDriver;
	}
}
