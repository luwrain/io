package org.luwrain.io.api.github.models;

/**
 * Represents a comment on a GitHub issue.
 */
public class Comment {
    private long id;
    private String body;
    private User user;

    /**
     * Represents a GitHub user associated with a comment.
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
     * Returns the unique identifier of this comment.
     * @return the comment ID
     */
    public long getId() { return id; }

    /**
     * Returns the body text of this comment.
     * @return the comment body
     */
    public String getBody() { return body; }

    /**
     * Returns the login of the comment author.
     * @return the author login, or "Anon" if unknown
     */
    public String getAuthor() { return (user != null) ? user.getLogin() : "Anon"; }

    @Override
    public String toString() {
        return "[" + getAuthor() + "]: " + body;
    }
}
