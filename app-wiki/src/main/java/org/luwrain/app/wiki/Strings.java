// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.wiki;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String actionNewServer();
    String actionServers();
    String appName();
    String defaultServerName();
    String newServerPopupName();
    String newServerPopupPrefix();
    String serverParamsAreaName();
    String serversAreaName();
}
