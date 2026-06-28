
package org.luwrain.popups;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.io.websearch.*;

public class WebSearchPopup extends ListPopupBase<Object> implements ListArea.ClickHandler<Object>
{
    protected final Response resp;
    protected Entry result = null;

    public WebSearchPopup(Luwrain luwrain, String name, Response resp, Set<Popup.Flags> popupFlags)
    {
	super(luwrain, createParams(luwrain, name, resp), popupFlags);
	this.resp = resp;
		setListClickHandler(this);
    }

    public Entry result()
    {
		return result;
    }

    	@Override public boolean onListClick(ListArea area, int index, Object item)
    {
	if (item == null || !(item instanceof Entry))
	    return false;
	this.result = (Entry)item;
	return this.closing.doOk();
    }

    static protected ListArea.Params<Object> createParams(Luwrain luwrain, String name, Response resp)
    {
	final var params = new ListArea.Params<Object>();
	params.context = new DefaultControlContext(luwrain);
	params.flags = EnumSet.of(ListArea.Flags.EMPTY_LINE_TOP);
	params.name = name;
	params.model = new ListUtils.FixedModel<>(createListItems(resp));
	params.appearance = new Appearance(params.context);
	params.transition = new Transition(params.model);
	return params;
    }

    static protected Object[] createListItems(Response resp)
    {
	final var r = new ArrayList<Object>();
	for(var i: resp.getEntries())
	{
	    r.add(i);
	    r.add(i.getDisplayUrl());
	    r.add(i.getSnippet());
	}
	return r.toArray(new Object[r.size()]);
    }

    static public Entry open(Luwrain luwrain, Response resp)
    {
	final var popup = new WebSearchPopup(luwrain, resp.getQuery().getText(), resp, Popups.DEFAULT_POPUP_FLAGS);
	luwrain.popup(popup);
	if (popup.wasCancelled())
	    return null;
	final Entry result = popup.result();
	return result;
    }

    static protected class Appearance extends ListUtils.DoubleLevelAppearance<Object>
    {
	public Appearance(ControlContext context)
	{
	    super(context);
	}
	
	@Override public boolean isSectionItem(Object obj)
	{
	    return obj instanceof Entry;
	}
    }

    static protected class Transition extends ListUtils.DoubleLevelTransition<Object>
    {
	public Transition(ListArea.Model<Object> model)
	{
	    super(model);
	}
	
	@Override public boolean isSectionItem(Object obj)
	{
	    return obj instanceof Entry;
	}
    }
}
