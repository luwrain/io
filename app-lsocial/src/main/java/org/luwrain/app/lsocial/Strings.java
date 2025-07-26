/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.lsocial;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();
    String accessTokenEdit();
    String optionsAreaName();
    String newEntryAreaName();
    String presentationListSuffix();
    String publicationListSuffix();

    //context menu items
    String create();
    String delete();

    //Forms and wizards
    String newPresentationAreaName();
    String nameEdit();
    String titleEdit();
    String authorsEdit();
    String subjectEdit();
    String dateEdit();
    String presentationClickable();
    String paperClickable();
    String bookClickable();
    String thesisClickable();
    String graduatingWorkClickable();
    String courseWorkClickable();
    

    //Popups
    String newMainListItemTypePopupName();

    //Messages
    String nameCannotBeEmpty();
}
