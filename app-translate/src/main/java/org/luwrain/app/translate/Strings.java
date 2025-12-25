// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.translate;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();
    String errNoYandexFolderId();
    String errNoYandexApiKey();
    String firstLangAreaName();
    String secondLangAreaName();
    String optionsAreaName();
}
