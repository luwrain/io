// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.ai;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.io.json.*;

import static java.util.Objects.*;

final class ParamsArea extends FormArea implements SectionArea
{
static private final String
    SYSTEM_PROMPT = "system-prompt",
            OPEN_AI_ENDPOINT = "open-ai-endpoint",
    OPEN_AI_API_KEY = "open-ai-api-key",
        OPEN_AI_MODEL = "open-ai-model",
            OPEN_AI_PROJECT = "open-ai-project",
    
        SPEECH_KIT_API_KEY = "speech-kit-api-key",
SPEECH_KIT_FOLDER_ID = "speech-kit-folder-id",
            TRANSLATOR_API_KEY = "translator-api-key",
    TRANSLATOR_FOLDER_ID = "translator-folder-id";

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
    }

    @Override public boolean saveSectionData()
    {
	final var l = controlPanel.getCoreInterface();
	l.updateConf(Config.class, c -> {
				c.setSystemPrompt(getEnteredText(SYSTEM_PROMPT));
		c.setOpenAiEndpoint(getEnteredText(OPEN_AI_ENDPOINT));
				c.setOpenAiApiKey(getEnteredText(OPEN_AI_API_KEY));
						c.setOpenAiModel(getEnteredText(OPEN_AI_MODEL));
								c.setOpenAiProject(getEnteredText(OPEN_AI_PROJECT));
	    });
	return true;
	    }

    @Override public boolean onInputEvent(InputEvent event)
    {
	if (controlPanel.onInputEvent(event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	if (controlPanel.onSystemEvent(event))
	    return true;
	return super.onSystemEvent(event);
    }
}
