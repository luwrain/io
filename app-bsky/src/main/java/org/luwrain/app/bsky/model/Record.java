// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bsky.model;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Record
{
    private String uri;
    private String cid;
    private String text;
    private String authorDid;
    private String authorHandle;
    private String authorDisplayName;
    private String authorAvatar;
    private String createdAt;
    private int replyCount;
    private int repostCount;
    private int likeCount;
    private int quoteCount;
    private Record replyTo;
    private Record quoteRecord;
    private List<Record> replies;
}
