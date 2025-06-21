package org.luwrain.io.api.osm;

import org.luwrain.io.api.osm.dto.*;
import org.luwrain.io.api.osm.model.*;
import java.util.List;

public class ElementMapper {
    public static Node map(NodeDto nodeDto) {
        return new Node(nodeDto.getId(), nodeDto.getTags(), nodeDto.getLat(), nodeDto.getLon());
    }

    public static Way map(WayDto wayDto, List<Node> nodes) {
        return new Way(wayDto.getId(), wayDto.getTags(), nodes);
    }

    public static Relation map(RelationDto relationDto, List<Element> members) {
        return new Relation(relationDto.getId(), relationDto.getTags(), members);
    }

    public static Coordinates map(CoordinatesDto coordDto) {
        return new Coordinates(coordDto.getType(), coordDto.getId(), coordDto.getDisplayName(),
                coordDto.getLat(), coordDto.getLon(), coordDto.getBoundingBox());
    }

    public static Address map(AddressDto addressDto) {
        return new Address(addressDto.getType(), addressDto.getId(), addressDto.getDisplayName());
    }
}
