// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String actionNewProfile();
    String actionDeleteProfile();
    String actionProfiles();
    String appName();
    String defaultProfileName();
    String errNoYandexFolderId();
    String errNoYandexApiKey();
    String filePrefix();
    String inputPrefix();
    String nameEdit();
    String newProfilePopupName();
    String newProfilePopupPrefix();
    String optionsAreaName();
    String profileDeletingPopupName();
    String profileDeletingPopupText(String profileName);
    String profileParamsAreaName();
    String profilePropNameCannotBeEmpty();
    String profilesAreaName();
    String systemPromptEdit();
    String openAiEndpointEdit();
    String openAiApiKeyEdit();
    String openAiModelEdit();
    String openAiProjectEdit();
    String temperatureEdit();
    String timeoutEdit();
    String outputLenLimitEdit();
    String yandexApiKeyEdit();
    String yandexFolderIdEdit();
}
