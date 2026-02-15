// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>
package org.luwrain.app.atessera.layouts;


import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.atessera.*;

import static java.util.Objects.*;

public final class PublicationPropertiesLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

    static private final String
	NAME = "name",
	SUBJECT = "subject",
	DESCR = "descr",
	TITLE = "title",
	SUBTITLE = "subtitle",
	AUTHORS = "authors",
	DATE = "date",
	LOCATION = "location",
	KEYWORDS = "keywords",
	PUBLISHED = "published",
	COPYRIGHT = "copyright",
	ISBN = "isbn",
	UDK = "udk",
	BBK = "bbk",
	ST_GROUP = "st-пroup",
	SP_NUM = "sp-num",
	SP_NAME = "sp-name",
	SV_NAME = "sv-name",
	SV_DEGREE = "sv-degree",
	SV_RANK = "sv-rank",
	TITLE_PAGE_TOP_NOTE = "title-page-top-note",
	TITLE_PAGE_BOTTOM_NOTE = "title-page-bottom-note",
	BOOK_TYPE = "book-type";

    final App app;
    final FormArea form;
    final Publication publication;

    public PublicationPropertiesLayout(App app, Publication publication, ActionHandler close)
    {
        super(app);
        this.app = app;
        this.publication = publication;
        final var s = app.getStrings();
        form = new FormArea(getControlContext(), s.publPropertiesAreaName());
        form.addEdit(NAME, s.nameEdit(), requireNonNullElse(publication.getName(), ""));
        form.addEdit(SUBJECT, s.subjectEdit(), requireNonNullElse(publication.getSubject(), ""));
        form.addEdit(DESCR, s.descrEdit(), requireNonNullElse(publication.getDescr(), ""));
        form.addEdit(TITLE, s.titleEdit(), requireNonNullElse(publication.getTitle(), ""));
        form.addEdit(SUBTITLE, s.subtitleEdit(), requireNonNullElse(publication.getSubtitle(), ""));
        form.addEdit(AUTHORS, s.authorsEdit(), requireNonNullElse(publication.getAuthors(), ""));
	form.addEdit(DATE, s.dateEdit(), requireNonNullElse(publication.getDate(), ""));
	form.addEdit(LOCATION, s.locationEdit(), requireNonNullElse(publication.getLocation(), ""));
	form.addEdit(KEYWORDS, s.keywordsEdit(), requireNonNullElse(publication.getKeywords(), ""));
	form.addEdit(PUBLISHED, s.publishedEdit(), requireNonNullElse(publication.getPublished(), ""));
	form.addEdit(COPYRIGHT, s.copyrightEdit(), requireNonNullElse(publication.getCopyright(), ""));
	form.addEdit(ISBN, s.isbnEdit(), requireNonNullElse(publication.getIsbn(), ""));
	form.addEdit(UDK, s.udkEdit(), requireNonNullElse(publication.getUdk(), ""));
	form.addEdit(BBK, s.bbkEdit(), requireNonNullElse(publication.getBbk(), ""));
	form.addEdit(ST_GROUP, s.stGroupEdit(), requireNonNullElse(publication.getStGroup(), ""));
	form.addEdit(SP_NUM, s.spNumEdit(), requireNonNullElse(publication.getSpNum(), ""));
	form.addEdit(SP_NAME, s.spNameEdit(), requireNonNullElse(publication.getSpName(), ""));
	form.addEdit(SV_NAME, s.svNameEdit(), requireNonNullElse(publication.getSvName(), ""));
	form.addEdit(SV_DEGREE, s.svDegreeEdit(), requireNonNullElse(publication.getSvDegree(), ""));
	form.addEdit(SV_RANK, s.svRankEdit(), requireNonNullElse(publication.getSvRank(), ""));
	form.addEdit(TITLE_PAGE_TOP_NOTE, s.titlePageTopNoteEdit(), requireNonNullElse(publication.getTitlePageTopNote(), ""));
	form.addEdit(TITLE_PAGE_BOTTOM_NOTE, s.titlePageBottomNoteEdit(), requireNonNullElse(publication.getTitlePageBottomNote(), ""));
	form.addEdit(BOOK_TYPE, s.bookTypeEdit(), requireNonNullElse(publication.getBookType(), ""));
        setAreaLayout(form, null);
        setOkHandler(() -> {
            publication.setName(form.getEnteredText("name"));
            publication.setSubject(form.getEnteredText("subject"));
            publication.setDescr(form.getEnteredText("descr"));
            publication.setTitle(form.getEnteredText("title"));
            publication.setSubtitle(form.getEnteredText("subtitle"));
            publication.setAuthors(form.getEnteredText("authors"));
            app.getLuwrain().saveConf(app.conf);
            close.onAction();
            return true;
        });
        log.debug("Setting close handler");
        setCloseHandler(close);
    }
}
