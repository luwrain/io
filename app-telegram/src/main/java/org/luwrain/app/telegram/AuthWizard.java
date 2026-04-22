
package org.luwrain.app.telegram;

import java.util.*;
import org.apache.logging.log4j.*;

import org.drinkless.tdlib.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.WizardArea.*;
import org.luwrain.app.base.*;
import org.luwrain.app.telegram.UpdatesHandler.InputWaiter;

import static org.drinkless.tdlib.TdApi.*;

final class AuthWizard extends LayoutBase implements Objects.NewInputWaiterListener
{
    static private final Logger log = LogManager.getLogger();
    
    private final App app;
    final WizardArea wizardArea;
    private String phoneNumber = "";

    AuthWizard(App app)
    {
	super(app);
	this.app = app;
	this.wizardArea = new WizardArea(getControlContext());
	setAreaLayout(wizardArea, null);
	app.getObjects().newInputWaiterListeners.add(this);
	//If the input waiter was created before the app launch
	if (app.core.getInputWaiter() != null)
	    onNewInputWaiter(app.core.getInputWaiter());
    }

    @Override public void onNewInputWaiter(InputWaiter inputWaiter)
    {
	switch(inputWaiter.type)
	{
	case PhoneNumber: {
	    final Frame frame = wizardArea.newFrame()
	    .addText(app.getStrings().authPhoneNumberIntro())
	    .addInput(app.getStrings().authPhoneNumberInput(), this.phoneNumber)
	    .addClickable(app.getStrings().authContinue(), (values)->{
		    this.phoneNumber = values.getText(0);
		    inputWaiter.setValue(values.getText(0));
		    return true;
		})
	    .addClickable(app.getStrings().authAddProxy(), this::onAddProxy);
	    wizardArea.show(frame);
	    break;
	}
	    	case AuthCode: {
	    final Frame frame = wizardArea.newFrame()
	    .addInput("Код авторизации:", "")
	    .addClickable("Продолжить", (values)->{
		    inputWaiter.setValue(values.getText(0));
		    return true;
		});
	    wizardArea.show(frame);
	    break;
	}
	}
    }

    boolean onAddProxy(WizardValues values)
    {
	String url = app.conv.addProxy();
	if (url == null)
	    return true;
	final var addr = new ProxyAddr(url.trim());
	if (!addr.valid)
	{
	    getLuwrain().playSound(Sounds.ERROR);
	    return true;
	}
	log.trace("Adding proxy {}", url);
	app.getOperations().getClient().send(new AddProxy(new Proxy(addr.host, addr.port, new ProxyTypeMtproto(addr.secret)), true), new Client.ResultHandler() {
		@Override public void onResult(TdApi.Object object)
		{
		    log.trace("Setting proxy result is {}", object.getClass().getName());
		    app.message(object.getClass().getName());
		}});
	return true;
    }
}
