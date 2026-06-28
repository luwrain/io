// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.searx;

import lombok.*;

@Data
@NoArgsConstructor
public final class Config
{
    static public final String DEFAULT_URL = "http://localhost:8888";

    private String url;
}
