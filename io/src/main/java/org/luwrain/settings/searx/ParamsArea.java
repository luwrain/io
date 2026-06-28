// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.searx;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;

import static java.util.Objects.*;

final class ParamsArea extends FormArea implements SectionArea
{
    static private final String
	URL = "url";

    private final ControlPanel controlPanel;

    ParamsArea(ControlPanel controlPanel)
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()),
	      controlPanel.getCoreInterface().i18n().getStaticStr("CpUiGeneral"));
	this.controlPanel = controlPanel;
	fillForm();
    }

    private void fillForm()
    {
	final var l = controlPanel.getCoreInterface();
	final var s = l.i18n().getStrings(Strings.class);
	final var c = requireNonNullElse(l.loadConf(Config.class), new Config());
	addEdit(URL, s.connectionAddress(), requireNonNullElse(c.getUrl(), ""));
    }

    @Override public boolean saveSectionData()
    {
	final var l = controlPanel.getCoreInterface();
		final var s = l.i18n().getStrings(Strings.class);
	final var url = getEnteredText(URL);
	if (url.isEmpty())
	{
	    l.message(s.noUrl(), Luwrain.MessageType.ERROR);
	    return false;
	}
	l.updateConf(Config.class, c -> {
	    c.setUrl(url);
	});
	return true;
    }

    @Override public boolean onInputEvent(InputEvent event)
    {
	if (controlPanel.onInputEvent(this, event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	if (controlPanel.onSystemEvent(this, event))
	    return true;
	return super.onSystemEvent(event);
    }
}
