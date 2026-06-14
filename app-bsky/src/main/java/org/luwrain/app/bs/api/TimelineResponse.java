// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs.api;

import java.util.*;

import lombok.*;
import org.luwrain.app.bs.model.Record;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TimelineResponse
{
    private List<Record> feed;
    private String cursor;

    public boolean hasMore()
    {
	return cursor != null && !cursor.isEmpty();
    }
}
