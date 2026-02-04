
package alpha4.json;

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
	return res;
    }

    

}
