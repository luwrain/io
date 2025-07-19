
package org.luwrain.app.mastodon;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

import static org.luwrain.popups.Popups.*;

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
