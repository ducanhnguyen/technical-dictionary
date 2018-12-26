package com.bugs_eclipse.matching;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

public class Commit {
	// Ex: jdt-core, jdt-apt
	private String product;

	private File productGitFile;

	private String fixedBugReport;

	private RevCommit rev;

	public String getId() {
		return rev.getName();
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getFixedBugReport() {
		return fixedBugReport;
	}

	public void setFixedBugReport(String fixedBugReport) {
		this.fixedBugReport = fixedBugReport;
	}

	public void setRev(RevCommit rev) {
		this.rev = rev;
	}

	public RevCommit getRev() {
		return rev;
	}

	public String getChangedFiles() {
		String changedFiles = "";

		try {
			// get repository
			File repoFile = productGitFile;
			FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
			Repository repository = null;
			try {
				repository = repositoryBuilder.setGitDir(repoFile).readEnvironment()
						// scan up the file system tree
						.findGitDir().setMustExist(true).build();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// The {tree} will return the underlying tree-id instead of the commit-id
			// itself!
			// For a description of what the carets do see e.g.
			// http://www.paulboxley.com/blog/2011/06/git-caret-and-tilde
			// This means we are selecting the parent of the parent of the parent of the
			// parent of current HEAD and
			// take the tree-ish of it

			if (rev.getParents().length >= 1) {
				System.out.println(productGitFile.getAbsolutePath());
				ObjectId oldHead = repository.resolve(rev.getParents()[0].getName());
				ObjectId head = repository.resolve(rev.getName());

				System.out.println("Printing diff between tree: " + oldHead + " and " + head);

				// prepare the two iterators to compute the diff between
				ObjectReader reader = repository.newObjectReader();
				CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
				oldTreeIter.reset(reader, oldHead);
				CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
				newTreeIter.reset(reader, head);

				// finally get the list of changed files
				Git git = new Git(repository);
				List<DiffEntry> diffs = git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call();
				for (DiffEntry entry : diffs) {
					changedFiles = changedFiles + entry.getNewPath() + ",";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return changedFiles;

	}

	public void setProductGitFile(File productGitFile) {
		this.productGitFile = productGitFile;
	}

	public File getProductGitFile() {
		return productGitFile;
	}

	@Override
	public String toString() {
		return getFixedBugReport() + "," + product + ", " + rev.getShortMessage() + "\n";
	}
}
