// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.ai;

import org.apache.logging.log4j.*;
import static java.util.Objects.*;

public interface Completion
{
    Completion endpoint(String endpoint);
    Completion apiKey(String apiKey);
    Completion model(String model);
    Completion project(String project);
    Completion addSystemPrompt(String content );
    Completion addUserMessage(String completion);
    Completion addAssistantMessage(String content);
    String querySincSingle();

    static public Completion newInstance(org.luwrain.settings.ai.Config conf)
    {
final Logger log = LogManager.getLogger();
	final var o = new OpenAi();
	if (conf != null)
	{
	    final String
	    endpoint = requireNonNullElse(conf.getOpenAiEndpoint(), "").trim(),
	    apiKey = requireNonNullElse(conf.getOpenAiApiKey(), "").trim(),
	    model = requireNonNullElse(conf.getOpenAiModel(), "").trim(),
	    project = requireNonNullElse(conf.getOpenAiProject(), "").trim(),
	    systemPrompt = requireNonNullElse(conf.getSystemPrompt(), "").trim();
	    if (endpoint.isEmpty())
	    {
		log.error("Unable to create an OpenAI client, no endpoint");
		return null;
	    }
	    	    if (apiKey.isEmpty())
	    {
		log.error("Unable to create an OpenAI client, no API key");
		return null;
	    }
		    o.endpoint(endpoint);
		    o.apiKey(apiKey);
		    if (!model.isEmpty())
			o.model(model);
		    if (!project.isEmpty())
			o.project(project);
		    if (!systemPrompt.isEmpty())
			o.addSystemPrompt(systemPrompt);
	}
	return o;
    }
}
