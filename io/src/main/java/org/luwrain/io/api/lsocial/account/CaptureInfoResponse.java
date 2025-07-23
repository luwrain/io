
package org.luwrain.io.api.lsocial.account;

import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class CaptureInfoResponse extends Response
{
    String imageUrl, audioUrl;
    int attemptsLeft;
}
