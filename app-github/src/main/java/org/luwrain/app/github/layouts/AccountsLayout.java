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
					 if (app.conf.getAccounts() == null)
					     app.conf.setAccounts(new ArrayList<>());
					 app.conf.getAccounts().add(account);
					 app.getLuwrain().saveConf(app.conf);
					 updateAccounts();
					 return true;
				     })
			      ));
	setCloseHandler(returnAction);
	updateAccounts();
    }

    void updateAccounts()
    {
	accounts.clear();
	if (app.conf.getAccounts() != null)
	    accounts.addAll(app.conf.getAccounts());
	//	model.clear();
	for (final var a : accounts)
	    //	    model.add(a.getName());
	area.refresh();
    }

    @Override public boolean onListClick(ListArea<Account> area, int index, Account account)
    {
		return true;
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
