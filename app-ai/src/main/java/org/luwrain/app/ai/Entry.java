// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Entry
{
    enum Type {USER, MODEL, FILE};

    private Type type;
    private String text;
    private String path;
}
