
package org.luwrain.io.api.lsocial.account;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public class ChangePasswordQuery extends Query<ChangePasswordQuery, ChangePasswordResponse>
{
static public final String
    ADDR = "/v1/account/chpwd/",
        ARG_NICK_NAME = "nickname",
    ARG_OLD_PWD = "oldpwd",
    ARG_NEW_PWD = "newpwd";

    public ChangePasswordQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, ChangePasswordResponse.class);
    }

    public ChangePasswordQuery nickName(String value)
    {
	args.put(ARG_NICK_NAME, requireNonNull(value, "value can't be null"));
	return this;
    }

        public ChangePasswordQuery oldPasswd(String value)
    {
	args.put(ARG_OLD_PWD, requireNonNull(value, "value can't be null"));
	return this;
    }


        public ChangePasswordQuery newPasswd(String value)
    {
	args.put(ARG_NEW_PWD, requireNonNull(value, "value can't be null"));
	return this;
    }
}
