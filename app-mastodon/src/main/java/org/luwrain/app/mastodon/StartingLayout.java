
package org.luwrain.app.mastodon;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.pim.mail.*;
import org.luwrain.app.base.*;

import org.luwrain.controls.WizardArea.Frame;
import org.luwrain.controls.WizardArea.WizardValues;

import static org.luwrain.core.DefaultEventResponse.*;

final class StartingLayout extends LayoutBase
{
    final App app;
    final Data data;
    final WizardArea wizardArea;
    final Frame introFrame;

    StartingLayout(App app)
    {
	super(app);
	this.app = app;
	this.data = app.getData();
	wizardArea = new WizardArea(getControlContext()) ;
	this.introFrame = wizardArea.newFrame()
	.addText(app.getStrings().wizardIntro())
	.addInput(app.getStrings().wizardName(), "supercoolusername")
		.addInput(app.getStrings().wizardMail(), "your@email.com")
			.addInput(app.getStrings().wizardPassword(), "password1234")
	.addClickable(app.getStrings().wizardContinue(), this::onMailAddress);
	wizardArea.show(introFrame);
	setAreaLayout(wizardArea, null);
    }

    private boolean onMailAddress(WizardValues values)
    {
	final String
	name = values.getText(0).trim(),
	mail = values.getText(1).trim(),
	passwd = values.getText(2);
	try {
	    final var token = data.client.registerAccount(name, mail, passwd, true, "en", "");
	    app.message(token.getAccessToken());
	}
	catch(IOException ex)
	{
	    app.crash(ex);
	}
	
	return true;
    }

}
