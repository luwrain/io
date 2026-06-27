// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github;

import java.io.*;
import java.nio.file.*;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();

    String accountsAreaName();
    String actionAccounts();
    String actionNewAccount();
    String actionDeleteAccount();
    String newAccountName();
    String newAccountNamePopupName();
    String newAccountNamePopupPrefix();
    String accountDeletingPopupName();
    String accountDeletingPopupText(String accountName);

    String reposAreaName();
    String pullRequestsAreaName();
    String searchAreaName();
    //    String actionRefresh();

    String accountPropName();
    String accountPropAccessToken();
    String accountPropDefault();
    String accountPropNameCannotBeEmpty();
    }
