
package org.luwrain.app.lsocial;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.io.api.yandex_gpt.*;
import org.luwrain.io.api.lsocial.presentation.Presentation;
import org.luwrain.io.api.lsocial.presentation.Frame;
import alpha4.json.Publication;
import alpha4.json.Publication.Section;
import org.luwrain.app.lsocial.layouts.*;
import alpha4.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;
import static org.luwrain.util.FileUtils.*;

public class MainLayout extends LayoutBase implements ListArea.ClickHandler<Object>
{
    static private final Logger log = LogManager.getLogger();

    public final List<Object> entries = new ArrayList<>();
    public final ListArea<Object> mainList;
    final Actions mainListActions;
    final App app;
    private LayoutExt ext = null;

    MainLayout(App app) 
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	mainList = new ListArea<Object>(listParams(p ->{
		    p.name = s.appName();
		    p.model = new ListModel<Object>(entries);
		    p.appearance = new MainListAppearance(getControlContext());
		    p.clickHandler = this;
		}));
	setPropertiesHandler(mainList, a -> new OptionsLayout(app, getReturnAction()));

	mainListActions = actions(
				  action("insert", s.create(), new InputEvent(Special.INSERT),
					 () -> { 	app.setAreaLayout(new NewEntryLayout(app, this, getReturnAction())); return true; }),
				  action("delete", s.delete(), new InputEvent(Special.DELETE), this::onMainListDelete)
				  );

	setAreaLayout(mainList, mainListActions);
    }

    @Override public boolean onListClick(ListArea<Object> area, int index, Object obj)
    {
	if (obj instanceof Publication publ)
	    return onPublClick(publ);
	return false;
    }

    boolean onPublClick(Publication publ)
    {
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
			            final var req = GetPublicationRequest.newBuilder()
				    .setPubl(String.valueOf(publ.getId()))
	    .build();
				    final var res = app.getPubl().get(req);
	    if (!res.getResultType().equals("OK"))
		throw new IllegalStateException("fixme");
	    

			
			
		app.finishedTask(taskId, () -> {
			openExt(new PublLayoutExt(this, Publication.fromGrpc(res.getPubl())));
		    });
	    });
    }

    boolean onMainListDelete()
    {
	final var selected = mainList.selected();
	if (selected == null)
	    return false;
	if (selected instanceof Publication publ)
	    return onDelete(publ);
	if (selected instanceof Presentation pr)
	    return onDelete(pr);
	return false;
    }

    boolean onDelete(Publication publ)
    {
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		            final var req = DeletePublicationRequest.newBuilder()
			    .setPubl(String.valueOf(publ.getId()))
	    .build();
			    final var res = app.getPubl().delete(req);
		app.finishedTask(taskId, () -> {
			//FIXME:
		    });
	    });
    }

    boolean onDelete(Presentation pr)
    {
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		final var res = new org.luwrain.io.api.lsocial.presentation.DeleteQuery(App.ENDPOINT)
		.accessToken(app.conf.getAccessToken())
		.pr(pr)
		.exec();
		app.finishedTask(taskId, () -> {
			//FIXME:
		    });
	    });
    }

    void openExt(LayoutExt ext)
    {
	this.ext = ext;
	    ext.setLayout();
	app.setAreaLayout(this);
	ext.activateDefaultArea();
    }

    public List<Object> fetchMainListItems() throws IOException
    {
	log.trace("Starting updating the main list");
	final var res = new ArrayList<Object>();
	final var req = ListPublicationsRequest.newBuilder().build();
	final var publRes = app.getPubl().list(req);
	//FIXME: Check OK
	for(int i = 0;i < publRes.getPublCount();i++)
	    res.add(Publication.fromGrpc(publRes.getPubl(i)));
	final var prRes = new org.luwrain.io.api.lsocial.presentation.ListQuery(App.ENDPOINT).accessToken(app.conf.getAccessToken()).exec();
	res.addAll(prRes.getEn());
	return res;
    }

    void updateMainList()
    {
	final var taskId = app.newTaskId();
	app.runTask(taskId, ()-> {
		final var res = fetchMainListItems();
		app.finishedTask(taskId, ()-> {
			entries.clear();
			entries.addAll(res);
			mainList.refresh();
		    });
	    });
    }

    final class MainListAppearance  extends DoubleLevelAppearance<Object>
    {
	MainListAppearance(ControlContext context) { super(context); }
	@Override public boolean isSectionItem(Object obj)
	{
	    return false;
	}

	@Override public void announceNonSection(Object item)
	{
	    final var s = app.getStrings();
	    if (item instanceof Publication publ)
		app.setEventResponse(listItem(publ.getName() + " " + s.publicationListSuffix())); else
		if (item instanceof Presentation pr)
		    app.setEventResponse(listItem(pr.getName() + " " + s.presentationListSuffix())); else
		    app.setEventResponse(listItem(item.toString()));
	}

	@Override public String getNonSectionScreenAppearance(Object item)
	{
	    if (item instanceof Publication publ)
		return publ.getName();
	    if (item instanceof Presentation pr)
		return pr.getName();
	    return item.toString();
	}
    }
}
