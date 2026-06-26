// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.app.base.*;
import org.luwrain.app.github.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

public final class AccountsLayout extends LayoutBase
{
    final App app;
    final List<Account> accounts;
    final ListArea<Account> accountsArea;

    public AccountsLayout(App app, ActionHandler closing)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	this.accounts = new ArrayList<>();
	if (app.conf.getAccounts() != null)
	    accounts.addAll(app.conf.getAccounts());

	this.accountsArea = new ListArea<Account>(listParams(p -> {
		    p.name = s.accountsAreaName();
		    p.model = new ListUtils.ListModel(accounts);
		    p.appearance = new Appearance();
		    p.clickHandler = (area, index, account) -> onClick(account);
		}));
	setCloseHandler(closing);
	setOkHandler(() -> {
		app.conf.setAccounts(accounts);
		getLuwrain().saveConf(app.conf);
		return closing.onAction();
		});
	setAreaLayout(accountsArea, actions(
					    
					    actNewAccount()
					    ));
    }

    boolean onClick(Account account)
    {
	// TODO: Open account properties layout
	return false;
    }

    boolean AccountInfo actNewAccount()
    {
	return action("insert", s.actionNewAccount(), new InputEvent(Special.INSERT), () -> {
	final String name = app.getConv().newAccountName();
	if (name == null)
	    return true;
	final Account a = new Account();
	a.setName(name);
	accounts.add(a);
	accountsArea.refresh();
	accountsArea.select(a, false);
	return true;
	    });
    }

    final class Appearance extends AbstractAppearance<Account>
    {
	@Override public void announceItem(Account account, Set<Flags> flags)
	{
	    app.setEventResponse(listItem(account.getName()));
	}

	@Override public String getScreenAppearance(Account account, Set<Flags> flags)
	{
	    return account.getName();
	}
    }
}
