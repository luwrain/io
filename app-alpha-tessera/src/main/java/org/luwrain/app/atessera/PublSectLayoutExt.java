// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.lsocial;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import alpha4.json.Publication;
import alpha4.json.Publication.Section;
import org.luwrain.app.lsocial.*;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;
import static org.luwrain.app.base.LayoutBase.*;

class PublSectLayoutExt implements LayoutExt
{
    static private final Logger log = LogManager.getLogger();

    final App app;
    final MainLayout mainLayout;
    final PublLayoutExt publLayout;
    final Publication publ;
    final Section sect;
    final int sectIndex;
    final EditArea edit;
    final Actions actions;

    PublSectLayoutExt(PublLayoutExt publLayout , Publication publ, Section sect, int sectIndex)
    {
	this.publLayout = publLayout;
	this.mainLayout = publLayout.mainLayout;
	this.app = mainLayout.app;
	this.publ = publ;
	this.sect = sect;
	this.sectIndex = sectIndex;

	edit = new EditArea(mainLayout.editParams( p -> {
		    p.editFactory = e -> {
			e.model = new MultilineEditModelWrap(e.model){
				@Override public MultilineEdit.ModificationResult putChars(int x, int y, String chars) { return super.putChars(x, y, app.translateUserInput(getLine(getHotPointY()), x, chars)); }
			    };
			return new MultilineEdit(e);
		    };
		})){
		@Override public boolean onSystemEvent(org.luwrain.core.events.SystemEvent event)
		{
		    if (event.getType() != org.luwrain.core.events.SystemEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case SAVE:
			return onSave();
		    default:
			return super.onSystemEvent(event);
		    }
		}
	    };
	final var text = requireNonNullElse(sect.getSource(), new ArrayList<>());
	edit.setText(text.toArray(new String[text.size()]));


	actions = mainLayout.actions(
				     //			  mainLayout.action("insert", s.create(), new InputEvent(Special.INSERT), this::onInsert)
);
    }

    boolean onSave()
    {
	//FIXME: Check len limit
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		/*FIXME:
		new org.luwrain.io.api.lsocial.publication.UpdateSectionQuery(App.ENDPOINT)
		.accessToken(app.conf.getAccessToken())
		.publ(String.valueOf(publ.getId()))
		.sect(String.valueOf(sectIndex))
		.source(edit.getTextAsList().stream().collect(joining("\n")))
		.exec();
		app.finishedTask(taskId, () -> {
			app.getLuwrain().playSound(Sounds.DONE);
		    });
		*/
	    });
    }

    @Override public void setLayout()
    {
	mainLayout.setAreaLayout(AreaLayout.LEFT_TOP_BOTTOM, mainLayout.mainList, mainLayout.mainListActions,
				 publLayout.sectList, publLayout.actions,
				 edit, actions);
    }

    @Override public void activateDefaultArea()
    {
	mainLayout.setActiveArea(edit);
    }
}
