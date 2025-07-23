
package org.luwrain.io.api.lsocial.chat;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public class SendTextMessageQuery extends Query<SendTextMessageQuery, SendTextMessageResponse>
{
    static public final String
	ADDR = "/v1/chat/send/text/",
    ARG_CHAT = "chat",
	ARG_TEXT = "text";

    public SendTextMessageQuery(String endpoint)
    {
	super(Type.POST, endpoint, ADDR, SendTextMessageResponse.class);
    }

    public SendTextMessageQuery chat(String value)
    {
	args.put(ARG_CHAT, requireNonNull(value, "value can't be null"));
	return this;
    }

        public SendTextMessageQuery text(String value)
    {
	args.put(ARG_CHAT, requireNonNull(value, "value can't be null"));
	return this;
    }
}
