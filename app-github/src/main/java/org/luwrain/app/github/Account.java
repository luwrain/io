// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Account
{
    private String name, accessToken;
    private boolean defaultAccount;
}
