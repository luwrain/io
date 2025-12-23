// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.github;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import static org.luwrain.io.api.Base.*;

public class GitHubClientTest
{
    static private final String TOKEN = System.getenv("LWR_GITHUB_TOKEN");

    @Test void myRepos()
    {
	if (!allowApiTests())
	    return;
	assertNotNull(TOKEN);
	assertFalse(TOKEN.isEmpty());
	final var c = new GitHubClient(TOKEN);
	assertTrue(c.connect());
	final var 	r = c.getMyRepos();
	assertNotNull(r);
	assertFalse(r.isEmpty());
    }

    @Test void search()
    {
	if (!allowApiTests())
	    return;
	assertNotNull(TOKEN);
	assertFalse(TOKEN.isEmpty());
	final var c = new GitHubClient(TOKEN);
	assertTrue(c.connect());
	final var r = c.search("luwrain");
	assertNotNull(r);
	assertFalse(r.isEmpty());
    }
}
