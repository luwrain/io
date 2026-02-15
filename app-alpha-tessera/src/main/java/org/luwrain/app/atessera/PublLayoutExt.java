// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.app.atessera.Publication.Section;
import org.luwrain.app.base.LayoutBase.ActionInfo;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;
import static org.luwrain.app.base.LayoutBase.*;

class PublLayoutExt implements LayoutExt
{
    static private final Logger log = LogManager.getLogger();

    final App app;
    final MainLayout mainLayout;
    final Publication publ;
    final ListArea<Section> sectList;
    final Actions actions;

    PublLayoutExt(MainLayout mainLayout, Publication publ)
    {
	this.app = mainLayout.app;
	this.mainLayout = mainLayout;
	this.publ = publ;
	final var s = mainLayout.app.getStrings();
	publ.setSections(new ArrayList<>(requireNonNullElse(publ.getSections(), List.of())));

	sectList = new ListArea<Section>(mainLayout.listParams(p ->{
		    p.name = requireNonNullElse(publ.getName(), "");
		    p.model = new ListModel<Section>(publ.getSections());
		    p.appearance = new Appearance();
		    p.clickHandler = (area, index, sect) -> onSectClick(sect);
		}));

	actions = mainLayout.actions(
				     mainLayout.action("insert", s.create(), new InputEvent(Special.INSERT), this::onInsert),
				     			  mainLayout.action("delete", s.delete(), new InputEvent(Special.DELETE), this::onDelete)
);
    }

    boolean onInsert()
    {
	final var type = app.conv.newPublSectType();
	if (type == null)
	    return true;
	final var beforeSect = sectList.selected();
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		final var sect = alpha4.PublicationSection.newBuilder()
		.setType(type)
		.build();
		final var req = alpha4.AddPublicationSectionRequest.newBuilder()
		.setPubl(String.valueOf(publ.getId()))
		.setSect(sect);
		if (beforeSect != null && !requireNonNullElse(beforeSect.getId(), "").isEmpty())
		    req.setBeforeSect(beforeSect.getId());
		final var res = app.getPubl().addSection(req.build());
		app.finishedTask(taskId, () -> {
			if (!app.okAnswer(res.getResultType(), res.getErrorMessage()))
			    return;
			final var newSect = Publication.fromGrpc(res.getSect());
			if (beforeSect != null)
			{
			    final int index = publ.getSections().indexOf(beforeSect);
			    if (index >= 0)
				publ.getSections().add(index, newSect); else
				publ.getSections().add(newSect);
			}else
			    publ.getSections().add(newSect);
			sectList.refresh();
			sectList.select(newSect, false);
		    });
	    });
    }

        boolean onDelete()
    {
	final var sect = sectList.selected();
	if (sect == null)
	    return false;
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		final var req = alpha4.RemovePublicationSectionRequest.newBuilder()
		.setPubl(String.valueOf(publ.getId()))
		.setSect(sect.getId())
		.build();
		final var res = app.getPubl().removeSection(req);
		app.finishedTask(taskId, () -> {
			if (!app.okAnswer(res.getResultType(), res.getErrorMessage()))
			    return;
			publ.getSections().remove(sect);
			sectList.refresh();
			app.getLuwrain().playSound(Sounds.DONE);
		    });
	    });
	    }


    boolean onSectClick(Section sect)
    {
	final var index = sectList.selectedIndex();
	if (index < 0)
	    return false;
	final var e = new PublSectLayoutExt(this, publ, publ.getSections().get(index), index);
	mainLayout.openExt(e);
	return true;
	    }

    @Override public void setLayout()
    {
	mainLayout.setAreaLayout(AreaLayout.LEFT_RIGHT, mainLayout.mainList,
				 mainLayout.mainListActions,
				 sectList, actions);
    }

    @Override public void activateDefaultArea()
    {
	mainLayout.setActiveArea(sectList);
    }

    final class Appearance extends AbstractAppearance<Section>
    {
	@Override public void announceItem(Section sect, Set<Flags> flags)
	{
	    switch(sect.getType())
	    {
	    case MARKDOWN:
	    case LATEX:
		if (sect.getSource() != null && !sect.getSource().isEmpty())
		app.getLuwrain().setEventResponse(listItem(sect.getSource().stream().collect(joining(" ")))); else
		    app.getLuwrain().setEventResponse(hint(Hint.EMPTY_LINE));
		return;
	    case METAPOST:
		app.getLuwrain().setEventResponse(listItem(app.getStrings().typeMetapost() + " "
							   + captOrSource(sect)));
		return;
	    case GNUPLOT:
		app.getLuwrain().setEventResponse(listItem(app.getStrings().typeGnuplot() + " "
							   + captOrSource(sect)));
		return;
	    case LISTING:
		app.getLuwrain().setEventResponse(listItem(app.getStrings().typeListing() + " "
							   + captOrSource(sect)));
		return;
	    default:
		app.getLuwrain().setEventResponse(listItem(captOrSource(sect)));
		break;
	    }
	}

	@Override public String getScreenAppearance(Section sect, Set<Flags> flags)
	{
	    switch(sect.getType())
	    {
	    case MARKDOWN:
	    case LATEX:
		if (sect.getSource() == null || sect.getSource().isEmpty())
		    return "";
		if (sect.getSource().size() == 1)
		    return sect.getSource().get(0);
		return sect.getSource().get(0) + "...";
	    default:
	        return captOrSource(sect);
	    }
	}

	String captOrSource(Section sect)
	{
	    final String capt;
	    if (sect.getCaption() != null && !sect.getCaption().isEmpty())
		capt = sect.getCaption().stream().collect(joining(" ")).trim(); else
		capt = "";
	    if (!capt.isEmpty())
		return capt;
	    final String source;
	    if (sect.getSource() != null && !sect.getSource().isEmpty())
		source = sect.getSource().stream().collect(joining(" ")).trim(); else
		source = "";
	    if (!source.isEmpty())
		return source;
	    return app.getStrings().textNotWrittenYet();
	    	}
    }

}
