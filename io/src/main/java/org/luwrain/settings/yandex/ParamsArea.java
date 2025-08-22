/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.settings.yandex;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.io.json.*;

import static java.util.Objects.*;

final class ParamsArea extends FormArea implements SectionArea
{
static private final String
    FOUN_MODELS_API_KEY = "foun-models-api-key",
        FOUN_MODELS_FOLDER_ID = "foun-models-folder-id",
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
	addEdit(FOUN_MODELS_API_KEY, s.founModelsApiKey(), requireNonNullElse(c.getFoundationModelsApiKey(), ""));
		addEdit(FOUN_MODELS_FOLDER_ID, s.founModelsFolderId(), requireNonNullElse(c.getFoundationModelsFolderId(), ""));
			addEdit(SPEECH_KIT_API_KEY, s.speechKitApiKey(), requireNonNullElse(c.getSpeechKitApiKey(), ""));
		addEdit(SPEECH_KIT_FOLDER_ID, s.speechKitFolderId(), requireNonNullElse(c.getSpeechKitFolderId(), ""));
					addEdit(TRANSLATOR_API_KEY, s.translatorApiKey(), requireNonNullElse(c.getTranslatorApiKey(), ""));
		addEdit(TRANSLATOR_FOLDER_ID, s.translatorFolderId(), requireNonNullElse(c.getTranslatorFolderId(), ""));
    }

    @Override public boolean saveSectionData()
    {
	final var l = controlPanel.getCoreInterface();
	l.updateConf(Config.class, c -> {
		c.setFoundationModelsApiKey(getEnteredText(FOUN_MODELS_API_KEY));
		c.setFoundationModelsFolderId(getEnteredText(FOUN_MODELS_FOLDER_ID));
		c.setSpeechKitApiKey(getEnteredText(SPEECH_KIT_API_KEY));
		c.setSpeechKitFolderId(getEnteredText(SPEECH_KIT_FOLDER_ID));
		c.setTranslatorApiKey(getEnteredText(TRANSLATOR_API_KEY));
		c.setTranslatorFolderId(getEnteredText(TRANSLATOR_FOLDER_ID));
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
