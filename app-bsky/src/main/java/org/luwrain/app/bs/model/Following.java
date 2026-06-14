// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs.model;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Following
{
    private String did;
    private String handle;
    private String displayName;
    private String avatar;
    private String description;
    private String createdAt;
}
