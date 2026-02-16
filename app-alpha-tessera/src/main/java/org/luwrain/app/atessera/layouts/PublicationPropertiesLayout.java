// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.atessera.*;

import static java.util.Objects.*;

public final class PublicationPropertiesLayout extends LayoutBase
{
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
	ORG = "org",
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
	form.addEdit(TITLE, s.titleEdit(), requireNonNullElse(publication.getTitle(), ""));
        form.addEdit(SUBTITLE, s.subtitleEdit(), requireNonNullElse(publication.getSubtitle(), ""));
        form.addEdit(SUBJECT, s.subjectEdit(), requireNonNullElse(publication.getSubject(), ""));
        form.addEdit(DESCR, s.descrEdit(), requireNonNullElse(publication.getDescr(), ""));
        form.addEdit(AUTHORS, s.authorsEdit(), requireNonNullElse(publication.getAuthors(), ""));
	form.addEdit(ORG, s.orgEdit(), requireNonNullElse(publication.getOrg(), ""));
	form.addEdit(DATE, s.dateEdit(), requireNonNullElse(publication.getDate(), ""));
	form.addEdit(LOCATION, s.locationEdit(), requireNonNullElse(publication.getLocation(), ""));
	form.addEdit(KEYWORDS, s.keywordsEdit(), requireNonNullElse(publication.getKeywords(), ""));
	form.addEdit(PUBLISHED, s.publishedEdit(), requireNonNullElse(publication.getPublished(), ""));
	form.addEdit(COPYRIGHT, s.copyrightEdit(), requireNonNullElse(publication.getCopyright(), ""));
	form.addEdit(ISBN, s.isbnEdit(), requireNonNullElse(publication.getIsbn(), ""));
	form.addEdit(UDK, s.udkEdit(), requireNonNullElse(publication.getUdk(), ""));
	form.addEdit(BBK, s.bbkEdit(), requireNonNullElse(publication.getBbk(), ""));
	form.addEdit(ST_GROUP, s.stGroupEdit(), requireNonNullElse(publication.getStGroup(), ""));
	form.addEdit(SP_NAME, s.spNameEdit(), requireNonNullElse(publication.getSpName(), ""));
	form.addEdit(SP_NUM, s.spNumEdit(), requireNonNullElse(publication.getSpNum(), ""));
	form.addEdit(SV_NAME, s.svNameEdit(), requireNonNullElse(publication.getSvName(), ""));
	form.addEdit(SV_DEGREE, s.svDegreeEdit(), requireNonNullElse(publication.getSvDegree(), ""));
	form.addEdit(SV_RANK, s.svRankEdit(), requireNonNullElse(publication.getSvRank(), ""));
	form.addEdit(BOOK_TYPE, s.bookTypeEdit(), requireNonNullElse(publication.getBookType(), ""));
	form.addEdit(TITLE_PAGE_TOP_NOTE, s.titlePageTopNoteEdit(), requireNonNullElse(publication.getTitlePageTopNote(), ""));
	form.addEdit(TITLE_PAGE_BOTTOM_NOTE, s.titlePageBottomNoteEdit(), requireNonNullElse(publication.getTitlePageBottomNote(), ""));
	
        setAreaLayout(form, null);
        setOkHandler(() -> {
		final String 
		name = form.getEnteredText(NAME).trim(),
		subject = form.getEnteredText(SUBJECT).trim(),
		descr = form.getEnteredText(DESCR).trim(),
		title = form.getEnteredText(TITLE).trim(),
		subtitle = form.getEnteredText(SUBTITLE).trim(),
		authors = form.getEnteredText(AUTHORS).trim();
		
		if (name.isEmpty())
		{
		    app.message(s.nameCannotBeEmpty(), Luwrain.MessageType.ERROR);
		    return true;
		}
		
		final var taskId = app.newTaskId();
		final var p = alpha4.Publication.newBuilder()
		.setName(name)
		.setTitle(title)
		.setSubtitle(subtitle)
		.build();
		final var res = app.getPubl().update(alpha4.UpdatePublicationRequest.newBuilder()
						     .setPubl(String.valueOf(publication.getId()))
						     .setNewPubl(p)
						     .build());
		return app.runTask(taskId, () -> {
			app.finishedTask(taskId, () -> {
				if (!app.okAnswer(res.getResultType(), res.getErrorMessage()))
				    return;
				publication.setName(name);
				publication.setTitle(title);
				publication.setSubtitle(subtitle);
				close.onAction();
			    });
		    });
	    });
        setCloseHandler(close);
    }
}
