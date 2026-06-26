package org.luwrain.io.api.github;

import org.luwrain.io.api.github.models.Comment;
import org.luwrain.io.api.github.models.Commit;
import org.luwrain.io.api.github.models.Issue;
import org.luwrain.io.api.github.models.Repo;
import java.util.List;

/**
 * Main interface for interacting with the GitHub API.
 */
public interface GitHubService {

    /**
     * Checks the connection and validates the token.
     * @return true if the authentication is successful
     */
    boolean connect();

    /**
     * Returns the list of repositories belonging to the current user.
     * @return list of repositories
     */
    List<Repo> getMyRepos();

    /**
     * Searches for repositories by a query string.
     * @param query search query (e.g. "java tetris")
     * @return list of matching repositories
     */
    List<Repo> search(String query);

    /**
     * Stars a repository.
     * @param repoFullName repository name in "owner/repo" format
     */
    void joinRepo(String repoFullName);

    /**
     * Removes a star from a repository.
     * @param repoFullName repository name in "owner/repo" format
     */
    void leaveRepo(String repoFullName);

    /**
     * Returns the list of open issues for a repository.
     * @param repoFullName repository name in "owner/repo" format
     * @return list of open issues
     */
    List<Issue> getIssues(String repoFullName);

    /**
     * Creates a new issue in the specified repository.
     * @param repoFullName repository name in "owner/repo" format
     * @param title the issue title
     * @param text the issue body text
     */
    void createIssue(String repoFullName, String title, String text);

    /**
     * Closes an issue in the specified repository.
     * @param repoFullName repository name in "owner/repo" format
     * @param issueNumber the issue number to close
     */
    void closeIssue(String repoFullName, int issueNumber);

    /**
     * Returns the list of comments for a specific issue.
     * @param repoFullName repository name in "owner/repo" format
     * @param issueNumber the issue number
     * @return list of comments
     */
    List<Comment> getComments(String repoFullName, int issueNumber);

    /**
     * Posts a new comment to a specific issue.
     * @param repoFullName repository name in "owner/repo" format
     * @param issueNumber the issue number
     * @param text the comment body text
     */
    void postComment(String repoFullName, int issueNumber, String text);

    /**
     * Returns the commit log for the specified repository.
     * @param repoFullName repository name in "owner/repo" format
     * @return list of recent commits
     */
    List<Commit> getCommits(String repoFullName);
}
