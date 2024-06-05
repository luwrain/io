package model;

import java.util.Map;
import java.util.List;

public class Relation extends Element{
    private List<Element> members;

    public Relation(long id, Map<String, String> tags, List<Element> members) {
        super(id, tags);
        this.members = members;
    }

    public List<Element> getMembers() {
        return members;
    }
}
