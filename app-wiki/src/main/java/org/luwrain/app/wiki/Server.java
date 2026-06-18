// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.wiki;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Server
{
    private String name, searchUrl, pagesUrl;

    @Override public String toString()
    {
	return this.name != null?this.name:"";
    }
}
