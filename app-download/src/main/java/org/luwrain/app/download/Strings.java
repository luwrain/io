// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.download;

public interface Strings
{
static final String NAME = "luwrain.download";

    String appName();
    String downloadAddingError(String details);
    String unableToMakeUrl(String url);
    String statusOk();
    String statusFailure();
}
