// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.app.atessera.Publication.Section;
import org.luwrain.app.atessera.*;
import alpha4.*;

import org.luwrain.app.base.LayoutBase.ActionInfo;
import org.luwrain.io.ai.Completion.Message;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;
import static org.luwrain.app.base.LayoutBase.*;

final class PublSectLayoutExt implements LayoutExt
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

		    p.appearance = new DefaultEditAreaAppearance(p.context){
			    @Override public void announceLine(int index, String line)
			    {
				if (line.trim().isEmpty())
				{
				    app.setEventResponse(hint(Hint.EMPTY_LINE));
				    return;
				}
				app.setEventResponse(text(app.getLuwrain().getSpeakableText(line, Luwrain.SpeakableTextType.PROGRAMMING)));
			    }
			};

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
				     actCompletion()
				     //			  mainLayout.action("insert", s.create(), new InputEvent(Special.INSERT), this::onInsert)
);
    }

    boolean onSave()
    {
	//FIXME: Check len limit
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		final var sect = PublicationSection.newBuilder()
		.setSource(edit.getTextAsList().stream().collect(joining("\n")))
		.build();
		final var req = UpdatePublicationSectionRequest.newBuilder()
		.setPubl(String.valueOf(publ.getId()))
		.setSect(this.sect.getId())
		.setNewSect(sect)
		.build();
		final var res = app.getPubl().updateSection(req);
		app.finishedTask(taskId, () -> {
			if (!app.okAnswer(res.getResultType(), res.getErrorMessage()))
			    return;
			app.getLuwrain().playSound(Sounds.DONE);
		    });
	    });
    }

    ActionInfo actCompletion()
    {
	return mainLayout.action("completion", app.getStrings().completion(),
				 new InputEvent(Special.F5),
				 () -> {
				     final var ai = new org.luwrain.io.ai.Completion();
				     if (!ai.load(app.getLuwrain()))
				     {
					 app.message(app.getStrings().aiEngineNotReady(), Luwrain.MessageType.ERROR);
					 return true;
				     }
				     final var prompt = app.conv.prompt();
				     if (prompt == null)
					 return true;
				     final var taskId = app.newTaskId();
				     app.runTask(taskId, () -> {
					     final var res = ai.splitLines(ai.generate(List.of(
											       new Message(Message.Type.SYSTEM, requireNonNullElse(app.conf.getSystemPrompt(), "")),
											       new Message(Message.Type.USER, prompt)
											       )));
					     app.finishedTask(taskId, () -> {
						     if (res != null && !res.isEmpty())
							 edit.update((text, hotPoint) -> {
								 for(var line: res)
								     text.add(line);
								 return false;
							     });
						     app.getLuwrain().playSound(Sounds.DONE);
						 });
					 });
				     return true;
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
