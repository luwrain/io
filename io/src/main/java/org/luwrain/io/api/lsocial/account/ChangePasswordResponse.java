
package org.luwrain.io.api.lsocial.account;

import lombok.*;
import org.luwrain.io.api.lsocial.*;

/*
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
*/
public class ChangePasswordResponse extends Response
{
static public final String
    INCORRECT_OLD_PASSWD = "INCORRECT_OLD_PASSWD",
    TOO_LONG_NEW_PASSWD = "TOO_LONG_NEW_PASSWD";
}
