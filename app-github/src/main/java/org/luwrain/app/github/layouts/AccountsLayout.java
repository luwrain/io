// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.app.base.*;

import org.luwrain.app.github.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;

public final class AccountsLayout extends LayoutBase implements ListArea.ClickHandler<Account>
{
    final App app;
    final List<Account> accounts = new ArrayList<>();
    final ListArea<Account> area;

    public AccountsLayout(App app, ActionHandler returnAction)
    {
	super(app);
	this.app = app;
	if (app.conf.getAccounts() != null)
	    accounts.addAll(app.conf.getAccounts());
	
	this.area = new ListArea<>(listParams(p -> {
		    p.name = app.getStrings().accountsAreaName();
		    p.model = new ListModel<Account>(accounts);
		    p.appearance = new Appearance();
		    p.clickHandler = this;
		}));
	setAreaLayout(area,
		      actions(
			      
			      action("new-account", app.getStrings().actionNewAccount(), new InputEvent(InputEvent.Special.INSERT),
				     () -> {
					 final var conv = app.getConv();
					 final var name = conv.newAccountName();
					 if (name == null || name.trim().isEmpty())
					     return true;
					 final var account = new Account(name.trim(), "", false);
					 accounts.add(account);
					 app.conf.setAccounts(accounts);
					 app.getLuwrain().saveConf(app.conf);
					 area.refresh();
					 area.select(account, false);
					 return true;
				     })
			      
			      ));
	setCloseHandler(returnAction);
    }

    @Override public boolean onListClick(ListArea<Account> area, int index, Account account)
    {
	app.setAreaLayout(new AccountPropertiesLayout(app, account, getReturnAction()));
	getLuwrain().announceActiveArea();
	return true;
    }

    final class Appearance extends AbstractAppearance<Account>
    {
	@Override public void announceItem(Account account, Set<Flags> flags)
	{
	    app.setEventResponse(listItem(account.getName(), Suggestions.CLICKABLE_LIST_ITEM));
	}

	@Override public String getScreenAppearance(Account account, Set<Flags> flags)
	{
	    return requireNonNullElse(account.getName(), "");
	}
    }
}
