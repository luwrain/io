// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.yandex.translate;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.luwrain.io.api.yandex.translate.model.*;
import static org.luwrain.io.api.Base.*;

public class YandexTranslateTest
{
    static private final String
	FOLDER_ID = System.getenv("YANDEX_FOLDER_ID"),
	API_KEY = System.getenv("YANDEX_API_KEY");

    @Test public void main() throws Exception
    {
	if (!allowApiTests())
	    return;
	assertNotNull(FOLDER_ID);
	assertFalse(FOLDER_ID.isEmpty());
	assertNotNull(API_KEY);
	assertFalse(API_KEY.isEmpty());
	final var req = new Request(FOLDER_ID, "en", List.of("Тест"));
	final var t = new YandexTranslate(API_KEY);
	final var resp = t.request(req);
	assertNotNull(resp);
	assertNotNull(resp.getTranslations());
	assertEquals(1, resp.getTranslations().size());
	assertNotNull(resp.getTranslations().get(0).getText());
	assertEquals("Test", resp.getTranslations().get(0).getText());
    }
}
