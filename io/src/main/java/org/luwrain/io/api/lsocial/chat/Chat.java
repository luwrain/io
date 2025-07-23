
package org.luwrain.io.api.lsocial.chat;

import java.util.*;
import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Chat
{
    static public final int
	TYPE_PRIVATE = 0,
	TYPE_GROUP = 1,
	TYPE_PUBLICATION = 3,
	TYPE_PRESENTATION = 3;

    private long id;
    private int type, numUnread;
    private String name;
    private Message lastMessage;
}
