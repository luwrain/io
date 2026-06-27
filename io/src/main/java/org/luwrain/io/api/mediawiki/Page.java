// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.mediawiki;

import static java.util.Objects.*;

/**
 * Represents a single page returned by a MediaWiki search query.
 *
 * <p>Each page contains a title, a short text snippet showing the search match
 * context, and the base URL of the wiki that returned it. The base URL points
 * to the MediaWiki API endpoint (e.g. {@code https://en.wikipedia.org/w/api.php}),
 * <em>not</em> to the human-readable page. To construct a page URL for reading,
 * use a separate wiki pages base URL (typically the {@code /wiki/} path) and
 * append the URL-encoded title.</p>
 *
 * <p>Instances are created by {@link Mediawiki} during search operations.</p>
 *
 * @see Mediawiki
 */
public final class Page
{
    private final String baseUrl;
    private final String title;
    private final String comment;

    /**
     * Creates a new Page.
     *
     * @param baseUrl the MediaWiki API base URL (e.g. {@code https://en.wikipedia.org/w/api.php});
     *                must not be null or empty
     * @param title   the page title; must not be null or empty
     * @param comment the search snippet showing match context; must not be null
     * @throws NullPointerException if any argument is null
     */
    public Page(String baseUrl, String title, String comment)
    {
	requireNonNull(baseUrl, "baseUrl can't be null");
	requireNonNull(title, "title can't be null");
	requireNonNull(comment, "comment can't be null");
	this.baseUrl = baseUrl;
	this.title = title;
	this.comment = comment;
    }

    /**
     * Returns the MediaWiki API base URL from which this page was retrieved.
     *
     * @return the API base URL (never null)
     */
    public String getBaseUrl()
    {
	return baseUrl;
    }

    /**
     * Returns the page title.
     *
     * @return the title (never null)
     */
    public String getTitle()
    {
	return title;
    }

    /**
     * Returns the search snippet text with HTML tags stripped.
     *
     * @return the comment snippet (never null, may be empty)
     */
    public String getComment()
    {
	return comment;
    }

    /**
     * Returns a human-readable representation of this page.
     * If the comment is non-empty, the result is {@code "Title, Comment"};
     * otherwise just the title.
     *
     * @return string representation of this page
     */
    @Override public String toString()
    {
	if (comment.trim().isEmpty())
	    return title.trim();
	return title.trim() + ", " + comment.trim();
    }
}
