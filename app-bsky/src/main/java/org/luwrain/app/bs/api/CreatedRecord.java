// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs.api;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class CreatedRecord
{
    private String uri;
    private String cid;
}
