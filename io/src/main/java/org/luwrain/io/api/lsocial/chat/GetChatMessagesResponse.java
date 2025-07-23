
package org.luwrain.io.api.lsocial.chat;

import java.util.*;
import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class GetChatMessagesResponse extends Response
{
    int numTotal, fromIndex, toIndex;
    List<Message> messages;
}
