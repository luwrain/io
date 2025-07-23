
package org.luwrain.io.api.lsocial.chat;

import java.util.*;
import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Message
{
static public final int
    TYPE_TEXT = 0;

    long id;
    private String type;
    private String sender;
}
