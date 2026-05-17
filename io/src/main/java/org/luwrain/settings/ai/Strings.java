// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.ai;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String name();
    String systemPrompt();
    String temperature();
    String noTemperature();
    String invalidTemperatureValue();
    String tooSmallTemperature();
    String tooLargeTemperature();
    String openAiApiKey();
    String openAiEndpoint();
    String openAiModel();
    String openAiProject();
}
