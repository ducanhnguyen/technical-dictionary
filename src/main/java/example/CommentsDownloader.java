package example;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Download the comments of a bug report on bugs.eclipse.org.
 * 
 * @author adn0019
 *
 */
public class CommentsDownloader {
	private URL bugReportLink;
	public List<String> comments = new ArrayList<String>();

	public static void main(String[] args) {
		CommentsDownloader commentDownloader = new CommentsDownloader();
		commentDownloader.setBugReportLink("https://bugs.eclipse.org/bugs/show_bug.cgi?id=112151");
		commentDownloader.extractComments();
		for (String comment : commentDownloader.getComments())
			System.out.println(comment + "\n------------------------");
	}

	public CommentsDownloader() {
	}

	public void extractComments() {

		if (bugReportLink != null) {
			String contentStr = crawlContent(bugReportLink);

			Document doc = Jsoup.parse(contentStr);
			Elements commentElements = doc.getElementsByClass("bz_comment_text");

			for (int i = 0; i < commentElements.size(); i++) {
				String comment = commentElements.get(i).text();
				comments.add(comment);
			}
		}
	}

	private String crawlContent(URL link) {
		String content = new String();
		Document doc = null;
		do {
			final int CONNECTION_TIMEOUT = 5000;
			try {
				doc = Jsoup.parse(link, CONNECTION_TIMEOUT);
			} catch (IOException e) {
				System.out.println("Can not parse the link. Wait for a few seconds!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} while (doc == null);

		content = doc.html();
		return content;
	}

	public void setBugReportLink(String bugReportLink) {
		try {
			this.bugReportLink = new URL(bugReportLink);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public URL getBugReportLink() {
		return bugReportLink;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

}
