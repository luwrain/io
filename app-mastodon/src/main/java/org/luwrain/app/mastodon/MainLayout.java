
package org.luwrain.app.mastodon;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.script.*;
import org.luwrain.app.base.*;
import org.luwrain.nlp.*;

final class MainLayout extends LayoutBase
{
    private final App app;
    final EditArea editArea;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.editArea = new EditArea(editParams((params)->{
		    params.name = "";
		}));
		setAreaLayout(editArea, null);
    }
}
