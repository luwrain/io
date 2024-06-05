package model;

import java.util.List;
import java.util.Map;

public class Way extends Element{
    private List<Node> nodes;

    public Way(long id, Map<String, String> tags, List<Node> nodes) {
        super(id, tags);
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
