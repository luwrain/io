package org.luwrain.io.api.github;

/**
 * Exception thrown when a GitHub API request fails.
 *
 * <p>This exception wraps both HTTP-level errors (status code 400 and above)
 * and network/connection errors. Use {@link #getStatusCode()} to distinguish
 * between them: a non-zero value indicates an HTTP error response.</p>
 */
public class GitHubException extends RuntimeException {
    private final int statusCode;

    /**
     * Creates a new exception with the given message.
     *
     * @param message the error message
     */
    public GitHubException(String message) {
        super(message);
        this.statusCode = 0;
    }

    /**
     * Creates a new exception with the given message and cause.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public GitHubException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
    }

    /**
     * Creates a new exception representing an HTTP error response.
     *
     * @param message    the error message
     * @param statusCode the HTTP status code
     */
    public GitHubException(String message, int statusCode) {
        super(message + " (HTTP " + statusCode + ")");
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP status code associated with this error.
     *
     * @return the HTTP status code, or 0 if this is not an HTTP error
     */
    public int getStatusCode() {
        return statusCode;
    }
}
