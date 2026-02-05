// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();

    //Section types
    String typeMarkdown();
    String typeLatex();
    String typeMetapost();
    String typeListing();
    String typeEquation();
    String typeGnuplot();
    String typeUml();
    String typeGraphviz();

    //config layout
    String accessTokenEdit();
        String yandexApiKeyEdit();
        String yandexFolderIdEdit();

    
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
    String graduationWorkClickable();
    String courseWorkClickable();
    

    //Popups
    String newPublSectTypePopupName();
    String newMainListItemTypePopupName();

    //Messages
    String nameCannotBeEmpty();
}
