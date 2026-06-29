// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Profile
{
    static public final double DEFAULT_TEMPERATURE = 0.5;

    private String name;
    private String
	systemPrompt, openAiEndpoint, openAiApiKey, openAiModel, openAiProject;
    private Double temperature;
    private Integer timeout, outputLenLimit;

    @Override public String toString()
    {
	return this.name != null?this.name:"";
    }
}
