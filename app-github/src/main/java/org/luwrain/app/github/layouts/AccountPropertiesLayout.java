// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.github.*;

import static java.util.Objects.*;

public final class AccountPropertiesLayout extends LayoutBase
{
    static private final String
	NAME = "name",
	ACCESS_TOKEN = "access-token",
	DEFAULT = "default";

    final App app;
    final FormArea form;
    final Account account;
    
    public AccountPropertiesLayout(App app, Account account, ActionHandler close)
    {
	super(app);
	this.app = app;
	this.account = account;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), requireNonNullElse(account.getName(), ""));
	form.addEdit(NAME, s.accountPropName(), requireNonNullElse(account.getName(), ""));
	form.addEdit(ACCESS_TOKEN, s.accountPropAccessToken(), requireNonNullElse(account.getAccessToken(), ""));
	form.addCheckbox(DEFAULT, s.accountPropDefault(), account.isDefaultAccount());
	setAreaLayout(form, null);
	setOkHandler(() -> {
		final var newName = form.getEnteredText(NAME).trim();
		if (newName.isEmpty())
		{
		    app.message(s.accountPropNameCannotBeEmpty(), Luwrain.MessageType.ERROR);
		    return true;
		}
		account.setName(newName);
		account.setAccessToken(form.getEnteredText(ACCESS_TOKEN).trim());
		account.setDefaultAccount(form.getCheckboxState(DEFAULT));
		close.onAction();
		return true;
	    });
	setCloseHandler(close);
    }
}
