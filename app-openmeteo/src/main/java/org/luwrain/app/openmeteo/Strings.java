// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.openmeteo;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();
    String inputPrefix();
    String optionsAreaName();
    String latitudeEdit();
    String longitudeEdit();
    String temperature();
    String windSpeed();
    String windSpeedUnit();
    String weatherCode();
    String weatherFor();
    String geocodingError();
    String forecastError();
}
