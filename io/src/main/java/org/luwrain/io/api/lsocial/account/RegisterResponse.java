
package org.luwrain.io.api.lsocial.account;

import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse extends Response
{
    static public final String
	NICK_NAME_ALREADY_IN_USE = "NICK_NAME_ALREADY_IN_USE";

    int captureId;
}
