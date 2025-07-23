
package org.luwrain.io.api.lsocial.chat;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public class GetPrivateChatQuery extends Query<GetPrivateChatQuery, GetPrivateChatResponse>
{
    static public final String
	ADDR = "/v1/chat/get/private/",
    ARG_ACCOUNT = "account";

    public GetPrivateChatQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, GetPrivateChatResponse.class);
    }

            public GetPrivateChatQuery limit(String value)
    {
	args.put(ARG_ACCOUNT, requireNonNull(value, "value can't be null"));
	return this;
    }
}
