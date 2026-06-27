// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.wiki;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String actionNewServer();
    String actionDeleteServer();
    String actionServers();
    String appName();
    String defaultServerName();
    String newServerPopupName();
    String newServerPopupPrefix();
    String serverParamsAreaName();
    String serversAreaName();
    String nameEdit();
    String searchUrlEdit();
    String pagesUrlEdit();
    String serverDeletingPopupName();
    String serverDeletingPopupText(String serverName);
    String serverPropNameCannotBeEmpty();
}
