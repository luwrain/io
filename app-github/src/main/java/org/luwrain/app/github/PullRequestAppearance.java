// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.list.*;
import org.luwrain.io.api.github.models.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class PullRequestAppearance extends AbstractAppearance<Issue>
{
    final private App app;

    PullRequestAppearance(App app)
    {
	this.app = app;
    }

    @Override public void announceItem(Issue issue, Set<Flags> flags)
    {
	final var sb = new StringBuilder();
	sb.append("#").append(issue.getNumber()).append(": ");
	sb.append(issue.getTitle());
	sb.append(" (").append(issue.getAuthor()).append(")");
	app.setEventResponse(listItem(sb.toString()));
    }

    @Override public String getScreenAppearance(Issue issue, Set<Flags> flags)
    {
	final var sb = new StringBuilder();
	sb.append("#").append(issue.getNumber()).append(" ");
	sb.append(issue.getTitle());
	sb.append(" [").append(issue.getAuthor()).append("]");
	return new String(sb);
    }
}
