package com.bugs_eclipse.matching;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.bugs_eclipse.utils.MyUtils;

/**
 * Math bug report in bugzilla with its commit in github
 * 
 * @author adn0019
 *
 */
public class Matching {
	public Map<String, File> githubLinks = null;
	public File csvBugReportFile = null;
	public File csvGitFile = null;

	public static void main(String[] args) {
		Map<String, File> githubLinks = new HashMap<String, File>();
		githubLinks.put("core", new File("C:/Users/adn0019/Desktop/JDT_MSR/eclipse.jdt.core/.git"));
		githubLinks.put("debug", new File("C:/Users/adn0019/Desktop/JDT_MSR/eclipse.jdt.debug/.git"));
		githubLinks.put("ui", new File("C:/Users/adn0019/Desktop/JDT_MSR/eclipse.jdt.ui/.git"));

		File csvBugReportFile = new File("C:/Users/adn0019/Desktop/JDT_MSR/jdt_data_full.csv");

		File csvGitFile = new File("C:/Users/adn0019/Desktop/JDT_MSR/jdt_considered_commits.csv");

		Matching matching = new Matching();
		matching.setCsvBugReportFile(csvBugReportFile);
		matching.setGithubLinks(githubLinks);
		matching.setCsvGitFile(csvGitFile);

		matching.match();
	}

	public void match() {
		List<Commit> consideredCommits = getFixedBugsId();
		//System.out.println(consideredCommits);
		//System.out.println(consideredCommits.size());

		// export to csv
		String csvContent = "\"Bug_ID\",\"Component\",Changed_files";
		int count = 0;
		for (Commit c : consideredCommits) {
			System.out.println(1.0f*count/consideredCommits.size());
			count++;
			
			String commitStr = c.getFixedBugReport() + "," + c.getProduct() + "," + c.getChangedFiles();
			csvContent = csvContent + "\n" + commitStr;
			
			if (count==100)
				break;
		}
		MyUtils.exportToFile(csvGitFile, csvContent);
	}

	private List<Commit> getFixedBugsId() {
		List<Commit> consideredCommits = new ArrayList<Commit>();
		if (githubLinks != null)
			for (String key : githubLinks.keySet()) {
				File gitFile = githubLinks.get(key);

				FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
				Repository repository = null;
				try {
					repository = repositoryBuilder.setGitDir(gitFile).readEnvironment().findGitDir().setMustExist(true)
							.build();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (repository != null) {
					Git git = new Git(repository);
					Iterable<RevCommit> logs = null;
					try {
						logs = git.log().call();
					} catch (NoHeadException e) {
						e.printStackTrace();
					} catch (GitAPIException e) {
						e.printStackTrace();
					}

					if (logs != null)
						for (RevCommit rev : logs) {
							String shortMessage = rev.getShortMessage();

							String fixedBugReport = "";
							Pattern pattern = Pattern.compile("Bug\\s+([0-9]+)");
							Matcher m = pattern.matcher(shortMessage);
							if (m.find()) {
								fixedBugReport = m.group(1);

								Commit c = new Commit();
								c.setRev(rev);
								c.setProduct(gitFile.getParentFile().getName());
								c.setFixedBugReport(fixedBugReport);
								c.setProductGitFile(gitFile);
								consideredCommits.add(c);
							}
						}
				}
			}
		return consideredCommits;
	}

	public Map<String, File> getGithubLinks() {
		return githubLinks;
	}

	public void setGithubLinks(Map<String, File> githubLinks) {
		this.githubLinks = githubLinks;
	}

	public File getCsvBugReportFile() {
		return csvBugReportFile;
	}

	public void setCsvBugReportFile(File csvBugReportFile) {
		this.csvBugReportFile = csvBugReportFile;
	}

	public void setCsvGitFile(File csvGitFile) {
		this.csvGitFile = csvGitFile;
	}

	public File getCsvGitFile() {
		return csvGitFile;
	}
}
