// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.ai;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.io.json.*;

import static java.util.Objects.*;
import static org.luwrain.util.TextUtils.*;

final class ParamsArea extends FormArea implements SectionArea
{
static private final String
    SYSTEM_PROMPT = "system-prompt",
    TEMPERATURE = "temperature",
    TIMEOUT = "timeout",
    OUTPUT_LEN_LIMIT = "output-len-limit",
            OPEN_AI_ENDPOINT = "open-ai-endpoint",
    OPEN_AI_API_KEY = "open-ai-api-key",
        OPEN_AI_MODEL = "open-ai-model",
    OPEN_AI_PROJECT = "open-ai-project";

    private final ControlPanel controlPanel;

    ParamsArea(ControlPanel controlPanel)
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()),
	      controlPanel.getCoreInterface().i18n().getStaticStr("CpUiGeneral"));
	this.controlPanel = controlPanel;
	fillForm();
    }

    private void fillForm()
    {
	final var l = controlPanel.getCoreInterface();
	final var s = l.i18n().getStrings(Strings.class);
	final var c = requireNonNullElse(l.loadConf(Config.class), new Config());
		addEdit(SYSTEM_PROMPT, s.systemPrompt(), requireNonNullElse(c.getSystemPrompt(), ""));
	addEdit(OPEN_AI_ENDPOINT, s.openAiEndpoint(), requireNonNullElse(c.getOpenAiEndpoint(), ""));
    	addEdit(OPEN_AI_API_KEY, s.openAiApiKey(), requireNonNullElse(c.getOpenAiApiKey(), ""));
    addEdit(OPEN_AI_MODEL, s.openAiModel(), requireNonNullElse(c.getOpenAiModel(), ""));
    addEdit(OPEN_AI_PROJECT, s.openAiProject(), requireNonNullElse(c.getOpenAiProject(), ""));
    addEdit(TEMPERATURE, s.temperature(), String.format("%.2f", requireNonNullElse(c.getTemperature(), Double.valueOf(Config.DEFAULT_TEMPERATURE))));
    }

    @Override public boolean saveSectionData()
    {
	final var l = controlPanel.getCoreInterface();
		final var s = l.i18n().getStrings(Strings.class);
	final var temperature = parseDoubleInBounds(l, getEnteredText(TEMPERATURE), 0.0, 1.0,
						    s.noTemperature(), s.invalidTemperatureValue(),
						    s.tooSmallTemperature(), s.tooLargeTemperature());
	if (temperature == null)
	    return false;
	l.updateConf(Config.class, c -> {
				c.setSystemPrompt(getEnteredText(SYSTEM_PROMPT));
		c.setOpenAiEndpoint(getEnteredText(OPEN_AI_ENDPOINT));
				c.setOpenAiApiKey(getEnteredText(OPEN_AI_API_KEY));
						c.setOpenAiModel(getEnteredText(OPEN_AI_MODEL));
								c.setOpenAiProject(getEnteredText(OPEN_AI_PROJECT));
								c.setTemperature(temperature);
	    });
	return true;
	    }

    @Override public boolean onInputEvent(InputEvent event)
    {
	if (controlPanel.onInputEvent(this, event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	if (controlPanel.onSystemEvent(this, event))
	    return true;
	return super.onSystemEvent(event);
    }
}
