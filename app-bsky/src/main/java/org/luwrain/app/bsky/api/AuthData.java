// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bsky.api;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class AuthData
{
    private String did;
    private String handle;
    private String accessJwt;
    private String refreshJwt;
    private String email;
    private boolean emailConfirmed;
}
