// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.translate;

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
