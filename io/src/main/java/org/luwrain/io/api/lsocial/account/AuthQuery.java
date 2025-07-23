
package org.luwrain.io.api.lsocial.account;

import org.luwrain.io.api.lsocial.*;
import static java.util.Objects.*;

public class AuthQuery extends Query<AuthQuery, AuthResponse>
{
static public final String
    ADDR = "/v1/account/auth/",
        ARG_NICK_NAME = "nickname",
    ARG_PASSWORD = "password";

    public AuthQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, AuthResponse.class);
    }

    public AuthQuery nickName(String value)
    {
	args.put(ARG_NICK_NAME, requireNonNull(value, "value can't be null"));
	return this;
    }

        public AuthQuery password(String value)
    {
	args.put(ARG_PASSWORD, requireNonNull(value, "value can't be null"));
	return this;
    }
}
