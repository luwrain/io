package org.luwrain.io.api.github.models;

/**
 * Represents a single commit in a repository.
 */
public class Commit
{
    private String sha;
    private CommitDetails commit;

    /**
     * Nested class holding the detailed commit information.
     */
    public static class CommitDetails
    {
        private String message;
        private AuthorInfo author;

        /**
         * Returns the commit message.
         * @return the commit message
         */
        public String getMessage() { return message; }

        /**
         * Returns the author information for this commit.
         * @return the author info
         */
        public AuthorInfo getAuthor() { return author; }
    }

    /**
     * Nested class holding the author's name and date.
     */
    public static class AuthorInfo
    {
        private String name;
        private String date;

        /**
         * Returns the author's name.
         * @return the author name
         */
        public String getName() { return name; }

        /**
         * Returns the commit date as an ISO 8601 string.
         * @return the commit date
         */
        public String getDate() { return date; }
    }

    /**
     * Returns the full SHA hash of this commit.
     * @return the commit SHA
     */
    public String getSha() { return sha; }

    /**
     * Returns the commit message.
     * @return the commit message, or empty string if unavailable
     */
    public String getMessage()
    {
        return commit != null ? commit.getMessage() : "";
    }

    /**
     * Returns the name of the commit author.
     * @return the author name, or "Unknown" if unavailable
     */
    public String getAuthorName()
    {
        return commit != null && commit.getAuthor() != null ? commit.getAuthor().getName() : "Unknown";
    }

    /**
     * Returns the date of the commit.
     * @return the commit date as ISO 8601 string, or empty string if unavailable
     */
    public String getDate()
    {
        return commit != null && commit.getAuthor() != null ? commit.getAuthor().getDate() : "";
    }

    @Override
    public String toString()
    {
        final var shortSha = sha != null && sha.length() >= 7 ? sha.substring(0, 7) : sha;
        return shortSha + " [" + getAuthorName() + "] " + getMessage();
    }
}
