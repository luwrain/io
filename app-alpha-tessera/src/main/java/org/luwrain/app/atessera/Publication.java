// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera;

import java.util.*;
import lombok.*;

import static java.util.Objects.*;

@Data
@NoArgsConstructor
public final class Publication
{
    public enum Type { GRADUATION_WORK, COURSE_WORK, PAPER, THESIS , BOOK};
    public enum SectionType {MARKDOWN, LATEX, EQUATION, TABLE, LISTING, METAPOST, GNUPLOT, PLANTUML, GRAPHVIZ_DOT, GRAPHVIZ_NEATO, GRAPHVIZ_TWOPI, GRAPHVIZ_CIRCO};

    private Type type;
    private long id;
    private String name, subject, descr, shortRef;
    private String title, subtitle, authors, date, location;
    private String published, org, keywords;
    private String isbn, copyright, udk, bbk;
    private String stGroup, spNum, spName;
    private String svName, svDegree, svRank;
    private String titlePageTopNote, titlePageBottomNote, bookType;
    private int numCols, pageWidth, pageHeight;
    private int pageTopMargin, pageLeftMargin, pageBottomMargin, pageRightMargin;
    
    @ToString.Exclude
	private Section abs;

    @ToString.Exclude
	private Section issueInfo;

    @ToString.Exclude
	private List<Section> sections;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static public final class Section
    {
	private SectionType type;
	private String id, label, listingLang;
	private List<String> source, alternativeSource, caption;

	@Override public boolean equals(Object o)
	{
	    if (id == null || id.isEmpty())
		return false;
	    if (o != null && o instanceof Section sect)
	    {
		if (sect.id.isEmpty())
		    return false;
		return id.equals(sect.id);
	    }
	    return false;
	}

	@Override public int hashCode()
	{
	    if (id == null || id.isEmpty())
		return 0;
	    return id.hashCode();
	}
    }

    static public Publication fromGrpc(alpha4.Publication src)
    {
	final var res = new Publication();
	if (src.hasType() && src.getType() != null)
	    switch(src.getType())
	    {
	    case BOOK: res.setType(Publication.Type.BOOK); break;
	    case PAPER: res.setType(Publication.Type.PAPER); break;
	    case THESIS: res.setType(Publication.Type.THESIS); break;
	    case GRADUATION_WORK: res.setType(Publication.Type.GRADUATION_WORK); break;
	    case COURSE_WORK: res.setType(Publication.Type.COURSE_WORK); break;
	    default:
		throw new IllegalArgumentException("Unknown type: " + src.getType().toString());
	    } else
	    res.setType(null);
	res.setId(src.hasId()?src.getId():-1);
	res.setName((src.hasName() && src.getName() != null && !src.getName().isEmpty()) ? src.getName() : null);
	res.setDescr((src.hasDescr() && src.getDescr() != null && !src.getDescr().isEmpty()) ? src.getDescr() : null);
	res.setSubject((src.hasSubject() && src.getSubject() != null && !src.getSubject().isEmpty()) ? src.getSubject() : null);
	res.setTitle((src.hasTitle() && src.getTitle() != null && !src.getTitle().isEmpty()) ? src.getTitle() : null);
	res.setSubtitle((src.hasSubtitle() && src.getSubtitle() != null && !src.getSubtitle().isEmpty()) ? src.getSubtitle() : null);
	res.setAuthors((src.hasAuthors() && src.getAuthors() != null && !src.getAuthors().isEmpty()) ? src.getAuthors() : null);
	res.setDate((src.hasDate() && src.getDate() != null) ? src.getDate() : null);
	res.setLocation((src.hasLocation() && src.getLocation() != null && !src.getLocation().isEmpty()) ? src.getLocation() : null);
	res.setPublished((src.hasPublished() && src.getPublished() != null) ? src.getPublished() : null);
	res.setKeywords((src.hasKeywords() && src.getKeywords() != null && !src.getKeywords().isEmpty()) ? src.getKeywords() : null);
	res.setOrg((src.hasOrg() && src.getOrg() != null && !src.getOrg().isEmpty()) ? src.getOrg() : null);
	res.setStGroup((src.hasStGroup() && src.getStGroup() != null && !src.getStGroup().isEmpty()) ? src.getStGroup() : null);
	res.setSpNum((src.hasSpNum() && src.getSpNum() != null && !src.getSpNum().isEmpty()) ? src.getSpNum() : null);
	res.setSpName((src.hasSpName() && src.getSpName() != null && !src.getSpName().isEmpty()) ? src.getSpName() : null);
	res.setSvName((src.hasSvName() && src.getSvName() != null && !src.getSvName().isEmpty()) ? src.getSvName() : null);
	res.setSvDegree((src.hasSvDegree() && src.getSvDegree() != null && !src.getSvDegree().isEmpty()) ? src.getSvDegree() : null);
	res.setSvRank((src.hasSvRank() && src.getSvRank() != null && !src.getSvRank().isEmpty()) ? src.getSvRank() : null);
	res.setIsbn((src.hasIsbn() && src.getIsbn() != null && !src.getIsbn().isEmpty()) ? src.getIsbn() : null);
	res.setCopyright((src.hasCopyright() && src.getCopyright() != null && !src.getCopyright().isEmpty()) ? src.getCopyright() : null);
	res.setUdk((src.hasUdk() && src.getUdk() != null && !src.getUdk().isEmpty()) ? src.getUdk() : null);
	res.setBbk((src.hasBbk() && src.getBbk() != null && !src.getBbk().isEmpty()) ? src.getBbk() : null);
	res.setTitlePageTopNote((src.hasTitlePageTopNote() && src.getTitlePageTopNote() != null && !src.getTitlePageTopNote().isEmpty()) ? src.getTitlePageTopNote() : null);
	res.setTitlePageBottomNote((src.hasTitlePageBottomNote() && src.getTitlePageBottomNote() != null && !src.getTitlePageBottomNote().isEmpty()) ? src.getTitlePageBottomNote() : null);
	res.setBookType((src.hasBookType() && src.getBookType() != null && !src.getBookType().isEmpty()) ? src.getBookType() : null);
	res.setPageWidth((src.hasPageWidth() && src.getPageWidth() > 0) ? src.getPageWidth() : -1);
	res.setPageHeight((src.hasPageHeight() && src.getPageHeight() > 0) ? src.getPageHeight() : -1);
	res.setPageTopMargin((src.hasPageTopMargin() && src.getPageTopMargin() > 0) ? src.getPageTopMargin() : -1);
	res.setPageLeftMargin((src.hasPageLeftMargin() && src.getPageLeftMargin() > 0) ? src.getPageLeftMargin() : -1);
	res.setPageBottomMargin((src.hasPageBottomMargin() && src.getPageBottomMargin() > 0) ? src.getPageBottomMargin() : -1);
	res.setPageRightMargin((src.hasPageRightMargin() && src.getPageRightMargin() > 0) ? src.getPageRightMargin() : -1);
	if (src.getSectsCount() > 0)
	{
	    final var num = src.getSectsCount();
	    final var a = new ArrayList<Section>();
	    a.ensureCapacity(num);
	    for(int i = 0;i < num;i++)
		a.add(fromGrpc(src.getSects(i)));
	    res.setSections(a);
	}
	return res;
    }

    static Section fromGrpc(alpha4.PublicationSection src)
    {
	final var res = new Section();
	if (src.hasType() && src.getType() != null)
	    switch(src.getType())
	    {
	    case  MARKDOWN: res.setType(SectionType.MARKDOWN); break;
	    case  LATEX: res.setType(SectionType.LATEX); break;
	    case  EQUATION: res.setType(SectionType.EQUATION); break;
	    case  TABLE: res.setType(SectionType.TABLE); break;
	    case  GNUPLOT: res.setType(SectionType.GNUPLOT); break;
	    case  PLANTUML: res.setType(SectionType.PLANTUML); break;
	    case  GRAPHVIZ_DOT: res.setType(SectionType.GRAPHVIZ_DOT); break;
	    case  GRAPHVIZ_NEATO: res.setType(SectionType.GRAPHVIZ_NEATO); break;
	    case  GRAPHVIZ_TWOPI: res.setType(SectionType.GRAPHVIZ_TWOPI); break;
	    case  GRAPHVIZ_CIRCO: res.setType(SectionType.GRAPHVIZ_CIRCO); break;
	    default:
		throw new IllegalArgumentException("Unknown section type: " + src.getType().toString());
	    } else
	    res.setType(null);
		res.setId((src.hasId() && !src.getId().isEmpty())?src.getId():null);
	res.setLabel((src.hasLabel() && !src.getLabel().isEmpty())?src.getLabel():null);
	res.setListingLang((src.hasListingLang() && !src.getListingLang().isEmpty())?src.getListingLang():null);
	res.setSource(src.hasSource()?splitLInes(src.getSource()):null);
	res.setAlternativeSource(src.hasAlternativeSource()?splitLInes(src.getAlternativeSource()):null);
	res.setCaption(src.hasCaption()?splitLInes(src.getCaption()):null);
	return res;
    }

    static private List<String> splitLInes(String str)
    {
	if (str == null)
	    return null;
	if (str.isEmpty())
	    return null;
	return Arrays.asList(str
			     .replaceAll("\r\n", "\n")
			     .replaceAll("\r", "\n")
			     .split("\n", -1));
    }
}
