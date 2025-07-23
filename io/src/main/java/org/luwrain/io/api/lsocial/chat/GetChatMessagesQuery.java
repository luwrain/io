
package org.luwrain.io.api.lsocial.chat;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public class GetChatMessagesQuery extends Query<GetChatMessagesQuery, GetChatMessagesResponse>
{
    static public final String
	ADDR = "/v1/chat/messages/list/",
	ARG_CHAT = "chat",
    ARG_OFFSET = "offset",
	ARG_LIMIT = "limit";

    public GetChatMessagesQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, GetChatMessagesResponse.class);
    }

        public GetChatMessagesQuery chat(String value)
    {
	args.put(ARG_CHAT, requireNonNull(value, "value can't be null"));
	return this;
    }


    public GetChatMessagesQuery offset(String value)
    {
	args.put(ARG_OFFSET, requireNonNull(value, "value can't be null"));
	return this;
    }

        public GetChatMessagesQuery limit(String value)
    {
	args.put(ARG_LIMIT, requireNonNull(value, "value can't be null"));
	return this;
    }
}
