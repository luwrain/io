// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai.layouts;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.ai.*;

import static java.util.Objects.*;

public final class OptionsLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

static private final String
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
	//		form.addEdit(YANDEX_FOLDER_ID, s.yandexFolderIdEdit(), requireNonNullElse(app.conf.getYandexFolderId(), ""));
	//	form.addEdit(YANDEX_API_KEY, s.yandexApiKeyEdit(), requireNonNullElse(app.conf.getYandexApiKey(), ""));
			setAreaLayout(form, null);
			setOkHandler(() -> {
				//				app.conf.setYandexFolderId(form.getEnteredText(YANDEX_FOLDER_ID));
				//				app.conf.setYandexApiKey(form.getEnteredText(YANDEX_API_KEY));
				app.getLuwrain().saveConf(app.conf);
				close.onAction();
				return true;
			    });
			log.debug("Setting close handler");
			setCloseHandler(close);
		    }
}
