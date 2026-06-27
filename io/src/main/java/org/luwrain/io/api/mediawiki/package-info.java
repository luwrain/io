// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

/**
 * Provides a client for the MediaWiki API.
 *
 * <p>The main entry point is {@link org.luwrain.io.api.mediawiki.Mediawiki Mediawiki},
 * which performs search queries against a MediaWiki instance (such as Wikipedia).
 * Search results are returned as a list of {@link org.luwrain.io.api.mediawiki.Page Page}
 * objects, each containing the page title, a text snippet with the search match,
 * and the base URL of the wiki API.</p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Mediawiki wiki = new Mediawiki("https://en.wikipedia.org/w/");
 * List<Page> results = wiki.search("Java programming");
 * for (Page p : results) {
 *     System.out.println(p.getTitle() + " — " + p.getComment());
 * }
 * }</pre>
 *
 * <p>The client communicates with the MediaWiki API using HTTP and parses
 * JSON responses via {@code com.google.gson.Gson Gson}.</p>
 *
 * @see org.luwrain.io.api.mediawiki.Mediawiki
 * @see org.luwrain.io.api.mediawiki.Page
 */
package org.luwrain.io.api.mediawiki;
