
package org.luwrain.io.ai;

public interface Completion
{
    Completion endpoint(String endpoint);
    Completion apiKey(String apiKey);
    Completion model(String model);
    Completion addSystemPrompt(String content );
    Completion addUserMessage(String completion);
}
