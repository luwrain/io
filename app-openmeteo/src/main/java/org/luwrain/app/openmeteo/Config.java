// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.openmeteo;

import lombok.*;

@Data
@NoArgsConstructor
public final class Config
{
    private String defaultLatitude = "55.7558";
    private String defaultLongitude = "37.6173";
}
