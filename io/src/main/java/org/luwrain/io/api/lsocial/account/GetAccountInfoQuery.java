
package org.luwrain.io.api.lsocial.account;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public class GetAccountInfoQuery extends Query<GetAccountInfoQuery, GetAccountInfoResponse>
{
    static public final String
	ADDR = "/v1/account/info/",
	ARG_NICK_NAME = "nickname";

    public GetAccountInfoQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, GetAccountInfoResponse.class);
    }

    public GetAccountInfoQuery nickName(String value)
    {
	args.put(ARG_NICK_NAME, requireNonNull(value, "value can't be null"));
	return this;
    }
}
