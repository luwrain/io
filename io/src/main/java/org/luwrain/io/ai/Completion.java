// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.ai;

import java.util.*;
import java.io.*;
import lombok.*;

import org.luwrain.core.*;

public final class Completion
{
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static public final class Message
    {
        public enum Type { SYSTEM, USER, ASSISTANT }

        private Type type;
        private String text;
    }

        private org.luwrain.settings.yandex.Config conf = null;

    public boolean load(Luwrain luwrain)
    {
	conf = luwrain.loadConf(org.luwrain.settings.yandex.Config.class);
	return conf != null;
    }

    public String generate(List<Message> messages)  throws IOException
    {
		final var m = messages.stream()
		.map( e-> new org.luwrain.io.api.yandex_gpt.Message(e.getType().toString().toLowerCase(), e.getText()) )
.toList();
	final var g = new org.luwrain.io.api.yandex_gpt.YandexGpt(
								  conf.getFoundationModelsFolderId(),
conf.getFoundationModelsApiKey(),
				    new org.luwrain.io.api.yandex_gpt.CompletionOptions(false, 0.7, 4096),
m);
	final var resp = g.doSync();
	final var a = resp.getResult().getAlternatives();
		final var mm = a.get(0).getMessage();
return mm.getText();
}
    }
    
