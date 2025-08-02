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

package org.luwrain.app.lsocial.layouts;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.lsocial.*;

import static java.util.Objects.*;

public final class OptionsLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

static private final String
    ACCESS_TOKEN = "access-token",
    YANDEX_FOLDER_ID = "yandex-folder-id",
	YANDEX_API_KEY = "yandex-api-key";

    final App app;
    final FormArea form;

    public OptionsLayout(App app, ActionHandler close)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), s.optionsAreaName());
		form.addEdit(ACCESS_TOKEN, s.accessTokenEdit(), requireNonNullElse(app.conf.getAccessToken(), ""));
				form.addEdit(YANDEX_FOLDER_ID, s.yandexFolderIdEdit(), requireNonNullElse(app.conf.getYandexFolderId(), ""));
						form.addEdit(YANDEX_API_KEY, s.yandexApiKeyEdit(), requireNonNullElse(app.conf.getYandexApiKey(), ""));
			setAreaLayout(form, null);
			setOkHandler(() -> {
				app.conf.setAccessToken(form.getEnteredText(ACCESS_TOKEN));
								app.conf.setYandexFolderId(form.getEnteredText(YANDEX_FOLDER_ID));
																app.conf.setYandexApiKey(form.getEnteredText(YANDEX_API_KEY));
				app.getLuwrain().saveConf(app.conf);
				close.onAction();
				return true;
			    });
			log.debug("Setting close handler");
			setCloseHandler(close);
		    }
}
