// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs.model;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class User
{
    private String did;
    private String handle;
    private String displayName;
    private String description;
    private String avatar;
    private String banner;
    private int followersCount;
    private int followsCount;
    private int postsCount;
    private String indexedAt;
    private boolean followedByMe;
}
