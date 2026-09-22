// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.download;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
public final class Config
{
    static public final String COMPLETED = "completed";
    static public final String FAILED = "failed";

    private List<Item> items = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static final class Item
    {
	private String url;
	private String destFile;
	private String status;
	private String errorInfo;
    }
}
