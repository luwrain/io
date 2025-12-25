// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.translate;

import java.util.*;
import java.time.*;
import java.util.concurrent.*;
import java.io.*;

/*
import yandex.cloud.sdk.*;
import yandex.cloud.sdk.auth.*;
import yandex.cloud.api.ai.translate.v2.*;
import yandex.cloud.api.ai.translate.v2.TranslationServiceGrpc.*;
*/

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

@AppNoArgs(
	   name = "trans",
	   title = { "en=Translator", "ru=Переводчик"}, 
	   category = StarterCategory.COMMUNICATIONS)
public final class App extends AppBase<Strings>
{
    public Conv conv = null;
    public Config conf = null;
    org.luwrain.settings.yandex.Config yandexConf;
    private MainLayout mainLayout = null;
    public App() { super(Strings.class, "luwrain.commander"); }

    @Override public AreaLayout onAppInit()
    {
	conf = getLuwrain().loadConf(Config.class);
	if (conf == null)
	{
	    conf = new Config();
	    getLuwrain().saveConf(conf);
	}
	        yandexConf = getLuwrain().loadConf(org.luwrain.settings.yandex.Config.class);
	if (yandexConf == null)
	{
	    yandexConf = new org.luwrain.settings.yandex.Config();
	    getLuwrain().saveConf(conf);
	}
	conv = new Conv(this);
	mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    String translate()
    {
	/*
	        final var factory = ServiceFactory.builder()
                .credentialProvider(Auth.apiKeyBuilder().fromEnv("YC_OAUTH"))
                .requestTimeout(Duration.ofMinutes(1))
                .build();
        final var translationService = factory.create(TranslationServiceBlockingStub.class, TranslationServiceGrpc::newBlockingStub);
	*/
	return "";
    }
}
