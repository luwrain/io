
package org.luwrain.io.api.lsocial.chat;

import java.util.*;
import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse extends Response
{
    int numTotal, fromIndex, toIndex;
    List<Chat> chats;
}
