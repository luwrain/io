
package org.luwrain.io.api.lsocial.account;

import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse extends Response
{
    private String accessToken;
    private long validUntil;
    }
