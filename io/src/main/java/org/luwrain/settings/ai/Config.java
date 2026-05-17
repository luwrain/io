// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.ai;

import lombok.*;

@Data
@NoArgsConstructor
public final class Config
{
    static public final double DEFAULT_TEMPERATURE = 0.5;
    
    private String
	systemPrompt, openAiEndpoint, openAiApiKey, openAiModel, openAiProject;
    private Double temperature;
    private Integer timeout, outputLenLimit;
}
