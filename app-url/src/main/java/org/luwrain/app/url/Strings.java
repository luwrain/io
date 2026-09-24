// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.url;

public interface Strings
{
    String appName();
    String fetching();
    String fetched(String url);
    String fetchError(String url, String error);
    String unableToMakeUrl(String text);
    String enterUrl();
    String enterUrlTitle();
}
