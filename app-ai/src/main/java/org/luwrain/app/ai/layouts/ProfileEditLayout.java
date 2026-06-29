// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.ai.*;

import static java.util.Objects.*;

public final class ProfileEditLayout extends LayoutBase
{
    static private final String
	NAME = "name",
	SYSTEM_PROMPT = "systemPrompt",
	OPENAI_ENDPOINT = "openAiEndpoint",
	OPENAI_API_KEY = "openAiApiKey",
	OPENAI_MODEL = "openAiModel",
	OPENAI_PROJECT = "openAiProject",
	TEMPERATURE = "temperature",
	TIMEOUT = "timeout",
	OUTPUT_LEN_LIMIT = "outputLenLimit";

    final App app;
    final FormArea form;
    final Profile profile;

    public ProfileEditLayout(App app, Profile profile, ActionHandler close)
    {
	super(app);
	this.app = app;
	this.profile = profile;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), s.profileParamsAreaName());
	form.addEdit(NAME, s.nameEdit(), requireNonNullElse(profile.getName(), ""));
	form.addEdit(SYSTEM_PROMPT, s.systemPromptEdit(), requireNonNullElse(profile.getSystemPrompt(), ""));
	form.addEdit(OPENAI_ENDPOINT, s.openAiEndpointEdit(), requireNonNullElse(profile.getOpenAiEndpoint(), ""));
	form.addEdit(OPENAI_API_KEY, s.openAiApiKeyEdit(), requireNonNullElse(profile.getOpenAiApiKey(), ""));
	form.addEdit(OPENAI_MODEL, s.openAiModelEdit(), requireNonNullElse(profile.getOpenAiModel(), ""));
	form.addEdit(OPENAI_PROJECT, s.openAiProjectEdit(), requireNonNullElse(profile.getOpenAiProject(), ""));
	form.addEdit(TEMPERATURE, s.temperatureEdit(), profile.getTemperature() != null?profile.getTemperature().toString():"");
	form.addEdit(TIMEOUT, s.timeoutEdit(), profile.getTimeout() != null?profile.getTimeout().toString():"");
	form.addEdit(OUTPUT_LEN_LIMIT, s.outputLenLimitEdit(), profile.getOutputLenLimit() != null?profile.getOutputLenLimit().toString():"");
	setAreaLayout(form, null);
	setOkHandler(() -> {
		final var newName = form.getEnteredText(NAME).trim();
		if (newName.isEmpty())
		{
		    app.message(app.getStrings().profilePropNameCannotBeEmpty(), Luwrain.MessageType.ERROR);
		    return true;
		}
		profile.setName(newName);
		profile.setSystemPrompt(form.getEnteredText(SYSTEM_PROMPT).trim());
		profile.setOpenAiEndpoint(form.getEnteredText(OPENAI_ENDPOINT).trim());
		profile.setOpenAiApiKey(form.getEnteredText(OPENAI_API_KEY).trim());
		profile.setOpenAiModel(form.getEnteredText(OPENAI_MODEL).trim());
		profile.setOpenAiProject(form.getEnteredText(OPENAI_PROJECT).trim());
		try {
		    profile.setTemperature(Double.valueOf(form.getEnteredText(TEMPERATURE).trim()));
		}
		catch(NumberFormatException e)
		{
		    profile.setTemperature(null);
		}
		try {
		    profile.setTimeout(Integer.valueOf(form.getEnteredText(TIMEOUT).trim()));
		}
		catch(NumberFormatException e)
		{
		    profile.setTimeout(null);
		}
		try {
		    profile.setOutputLenLimit(Integer.valueOf(form.getEnteredText(OUTPUT_LEN_LIMIT).trim()));
		}
		catch(NumberFormatException e)
		{
		    profile.setOutputLenLimit(null);
		}
		app.getLuwrain().saveConf(app.conf);
		close.onAction();
		return true;
	    });
	setCloseHandler(close);
    }
}
