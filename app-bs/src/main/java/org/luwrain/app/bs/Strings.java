// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();

    //Main layout
    String mainAreaName();
    String quickPostAreaName();
    String quickPostHint();
    String post();

    //Followings layout
    String followingsAreaName();

    //User layout
    String userAreaName();
    String userRecordsAreaName();
    String userFollowingsAreaName();
    String userFollowersAreaName();

    //Settings layout
    String settingsAreaName();
    String handleEdit();
    String appPasswordEdit();

    //Actions
    String create();
    String delete();
    String refresh();
    String open();
    String follow();
    String unfollow();

    //Messages
    String nameCannotBeEmpty();
    String notConfigured();
    String recordPosted();

    //Record
    String recordSuffix();
    String repostedBy();
    String likedBy();
    String reply();
    String repost();
    String like();
    String quote();
}
