
package org.luwrain.app.lsocial.layouts;

import java.util.*;
import java.text.*;
import java.util.function.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.lsocial.*;

import alpha4.*;
//import alpha4.json.*;

import static java.util.Objects.*;

public final class NewEntryLayout extends LayoutBase
{
    enum Type { PR, PAPER, BOOK, THESIS, GRADUATION, COURSE };
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM YYYY г");

    final App app;
    final MainLayout mainLayout;
    final WizardArea wizard;
    final ActionHandler close;

    public NewEntryLayout(App app, MainLayout mainLayout, ActionHandler close)
    {
	super(app);
	this.app = app;
	this.mainLayout = mainLayout;
	this.close = close;
	final var s = app.getStrings();

	wizard = new WizardArea(getControlContext());
	wizard.setAreaName(s.newEntryAreaName());

	final var fr = wizard.newFrame()
	.addInput(s.nameEdit(), "")
	.addInput(s.subjectEdit(), "")
	.addInput(s.titleEdit(), "")
	.addInput(s.authorsEdit(), "")
	.addInput(s.dateEdit(), DATE_FORMAT.format(new Date()))
	.addClickable(s.presentationClickable(), v -> create(Type.PR, v))
	.addClickable(s.paperClickable(), v -> create(Type.PAPER, v))
	.addClickable(s.bookClickable(), v -> create(Type.BOOK, v))
	.addClickable(s.thesisClickable(), v -> create(Type.THESIS, v))
	.addClickable(s.graduationWorkClickable(), v -> create(Type.GRADUATION, v))
	.addClickable(s.courseWorkClickable(), v -> create(Type.COURSE, v));

	wizard.show(fr);
	setCloseHandler(close);
	setAreaLayout(wizard, null);
    }

    boolean create(Type type, WizardArea.WizardValues values)
    {
	final String
	name = values.getText(0).trim(),
	subject = values.getText(1).trim(),
	title = values.getText(2).trim(),
	authors = values.getText(3).trim(),
	date = values.getText(4);
	if (name.isEmpty())
	{
	    app.message(app.getStrings().nameCannotBeEmpty(), Luwrain.MessageType.ERROR);
	    return true;
	}

	if (type == Type.PR)
	{
	    final var taskId = app.newTaskId();
	    return app.runTask(taskId, () -> {
		    final var resp = new org.luwrain.io.api.lsocial.presentation.CreateQuery(App.ENDPOINT)
		    .accessToken(app.conf.getAccessToken())
		    .name(name)
		    .title(title)
		    .authors(authors)
		    .subject(subject)
		    .date(date)
		    .exec();
		    final var res = mainLayout.fetchMainListItems();
		    app.finishedTask(taskId, () -> {
			    mainLayout.entries.clear();
			    mainLayout.entries.addAll(res);
			    mainLayout.mainList.refresh();
			    close.onAction();
			});
		});
	}

	final PublicationType t;
	switch(type)
	{
	case PAPER:
	    t = PublicationType.PAPER;
	    break;
	case BOOK:
	    t = PublicationType.BOOK;
	    break;
	case THESIS:
	    t = PublicationType.THESIS;
	    break;
	case GRADUATION:
	    t = PublicationType.GRADUATION_WORK;
	    break;
	case COURSE:
	    t = PublicationType.COURSE_WORK;
	    break;
	default:
	    return false;
	}

	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		final var p = Publication.newBuilder()
				.setType(t)
		.setName(name)
		.setTitle(title)
		.setAuthors(authors)
		.setSubject(subject)
		.setDate(date)
		.build();
		final var req = CreatePublicationRequest.newBuilder()
		.setPubl(p)
		.build();
		final var res = app.getPubl().create(req);
		app.finishedTask(taskId, () -> {
			mainLayout.entries.clear();
			//			mainLayout.entries.addAll(res);
			mainLayout.mainList.refresh();
			close.onAction();
		    });
	    });
    }
}
