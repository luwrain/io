// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.openmeteo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Entry
{
    enum Type {QUERY, RESULT, ERROR};

    private Type type;
    private String text;
    private String location;
}
