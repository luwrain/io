
package org.luwrain.io.api.lsocial.chat;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public class ListQuery extends Query<ListQuery, ListResponse>
{
    static public final String
	ADDR = "/v1/chat/list/",
    ARG_OFFSET = "offset",
	ARG_LIMIT = "limit";

    public ListQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, ListResponse.class);
    }

    public ListQuery offset(String value)
    {
	args.put(ARG_OFFSET, requireNonNull(value, "value can't be null"));
	return this;
    }

        public ListQuery limit(String value)
    {
	args.put(ARG_LIMIT, requireNonNull(value, "value can't be null"));
	return this;
    }
}
