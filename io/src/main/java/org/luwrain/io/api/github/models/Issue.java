package org.luwrain.io.api.github.models;

/**
 * Represents a GitHub issue.
 */
public class Issue {
    private int number;
    private String title;
    private String body;
    private User user;

    /**
     * Represents a GitHub user associated with an issue.
     */
    public static class User {
        private String login;

        /**
         * Returns the user's login name.
         * @return the login
         */
        public String getLogin() { return login; }
    }

    /**
     * Returns the issue number.
     * @return the issue number
     */
    public int getNumber() { return number; }

    /**
     * Returns the issue title.
     * @return the title
     */
    public String getTitle() { return title; }

    /**
     * Returns the issue body text.
     * @return the body, or null if empty
     */
    public String getBody() { return body; }

    /**
     * Returns the login of the issue author.
     * @return the author login, or "Anon" if unknown
     */
    public String getAuthor() { return (user != null) ? user.getLogin() : "Anon"; }

    @Override
    public String toString() {
        return "[#" + number + "] " + title + " (by " + getAuthor() + ")";
    }
}
