/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.urlget;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.io.api.mediawiki.*;
import org.luwrain.popups.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleUtils.*;

final class Conversations
{
    private final Luwrain luwrain;
    private final Strings strings;

    Conversations(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

}
