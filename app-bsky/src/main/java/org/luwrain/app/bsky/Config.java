// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bsky;

import lombok.*;

@Data
@NoArgsConstructor
public final class Config
{
    private String handle, appPassword;
    private AuthData authData;
}
