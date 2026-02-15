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
        form.addEdit("name", s.nameEdit(), requireNonNullElse(publication.getName(), ""));
        form.addEdit("subject", s.subjectEdit(), requireNonNullElse(publication.getSubject(), ""));
        form.addEdit("descr", s.descrEdit(), requireNonNullElse(publication.getDescr(), ""));
        form.addEdit("title", s.titleEdit(), requireNonNullElse(publication.getTitle(), ""));
        form.addEdit("subtitle", s.subtitleEdit(), requireNonNullElse(publication.getSubtitle(), ""));
        form.addEdit("authors", s.authorsEdit(), requireNonNullElse(publication.getAuthors(), ""));

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
