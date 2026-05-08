// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.ai;

import java.util.*;

import com.openai.client.*;
import com.openai.client.okhttp.*;
import com.openai.models.*;
import com.openai.models.chat.completions.*;

import static java.util.Objects.*;

public class OpenAI implements Completion
{
    protected String endpoint, apiKey, model;
            final List<ChatCompletionMessageParam> messages = new ArrayList<>();

    @Override public OpenAI endpoint(String endpoint)
    {
	this.endpoint = requireNonNull(endpoint, "endpoint can't be null");
	return this;
    }

        @Override public OpenAI apiKey(String apiKey)
    {
	this.apiKey = requireNonNull(apiKey, "apiKey can't be null");
	return this;
    }

            @Override public OpenAI model(String model)
    {
	this.model = requireNonNull(model, "model can't be null");
	return this;
    }

    @Override public OpenAI addSystemPrompt(String content)
    {
	        messages.add(ChatCompletionMessageParam.ofSystem(
                ChatCompletionSystemMessageParam.builder()
                        .content(content)
                        .build()
        ));
		return this;
    }

    @Override public OpenAI addUserMessage(String content)
    {
	        messages.add(ChatCompletionMessageParam.ofUser(
                ChatCompletionUserMessageParam.builder()
                        .content("Как убить процесс по его имени?")
                        .build()
        ));
		return this;
    }

    public void query()
    {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();







        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model("gpt-4o-mini") 
                .messages(messages)
                .temperature(0.3)
                .maxTokens(512)
                .build();

        try {
            ChatCompletion completion = client.chat().completions().create(params);
            completion.choices().stream()
                    .findFirst()
                    .ifPresent(choice -> {
                        System.out.println("Ответ модели:");
                        System.out.println(choice.message().content().orElse(""));
                    });

        }
	catch (Exception e)
	{
            System.err.println("Произошла ошибка при обращении к OpenAI API: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
