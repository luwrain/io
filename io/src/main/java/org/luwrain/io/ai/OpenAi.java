// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.ai;

import java.util.*;
import java.util.concurrent.atomic.*;
import org.apache.logging.log4j.*;

import com.openai.client.*;
import com.openai.client.okhttp.*;
import com.openai.models.*;
import com.openai.models.chat.completions.*;

import static java.util.Objects.*;

public class OpenAi implements Completion
{
    protected String endpoint, apiKey, model, project;
    static private final Logger log = LogManager.getLogger();
    
            final List<ChatCompletionMessageParam> messages = new ArrayList<>();

    @Override public OpenAi endpoint(String endpoint)
    {
	this.endpoint = requireNonNull(endpoint, "endpoint can't be null");
	return this;
    }

        @Override public OpenAi apiKey(String apiKey)
    {
	this.apiKey = requireNonNull(apiKey, "apiKey can't be null");
	return this;
    }

            @Override public OpenAi model(String model)
    {
	this.model = requireNonNull(model, "model can't be null");
	return this;
    }

                @Override public OpenAi project(String project)
    {
	this.project = requireNonNull(project, "project can't be null");
	return this;
    }


    @Override public OpenAi addSystemPrompt(String content)
    {
	        messages.add(ChatCompletionMessageParam.ofSystem(
                ChatCompletionSystemMessageParam.builder()
                        .content(content)
                        .build()
        ));
		log.trace("The system prompt added ({}).", content.length() > 80?content.substring(0, 80):content);
		return this;
    }

    @Override public OpenAi addUserMessage(String content)
    {
	requireNonNull(content, "content can't be null");
	        messages.add(ChatCompletionMessageParam.ofUser(
                ChatCompletionUserMessageParam.builder()
                        .content(content)
                        .build()
        ));
				log.trace("The user message added ({}).", content.length() > 80?content.substring(0, 80):content);
		return this;
    }

        @Override public OpenAi addAssistantMessage(String content)
    {
		requireNonNull(content, "content can't be null");
	        messages.add(ChatCompletionMessageParam.ofAssistant(
                ChatCompletionAssistantMessageParam.builder()
                        .content(content)
                        .build()
        ));
		return this;
    }


    @Override public String querySincSingle()
    {
	log.trace("Querying in sink mode the {} model at {}", model, endpoint);
        final var client = OpenAIOkHttpClient.builder()
	.baseUrl(endpoint)
                .apiKey(apiKey)
                .build();
        final var params = ChatCompletionCreateParams.builder()
                .model(model) 
                .messages(messages)
                .temperature(0.5)
                .maxTokens(1048576)
                .build();
final var completion = client.chat().completions().create(params);
final var res = new AtomicReference<String>();
            completion.choices().stream()
                    .findFirst()
                    .ifPresent(choice -> res.set(choice.message().content().orElse("")));
	    if (res.get() != null)
	    {
		log.trace("Getting the response: {}", res.get().length() > 80?res.get().substring(0, 80):res.get());
		addAssistantMessage(res.get());
	    } else
		log.trace("No text in response");
	    return res.get();
    }
}
