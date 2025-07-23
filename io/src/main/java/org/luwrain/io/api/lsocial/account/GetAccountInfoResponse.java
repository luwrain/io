
package org.luwrain.io.api.lsocial.account;

import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountInfoResponse extends Response
{
    Account account;
}
