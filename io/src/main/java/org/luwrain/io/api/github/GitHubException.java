package org.luwrain.io.api.github;

public class GitHubException extends RuntimeException {
    private final int statusCode;

    public GitHubException(String message) {
        super(message);
        this.statusCode = 0;
    }

    public GitHubException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
    }

    public GitHubException(String message, int statusCode) {
        super(message + " (HTTP " + statusCode + ")");
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}