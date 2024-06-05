package service;

import com.google.gson.*;
import dto.deserializer.ElementDtoDeserializer;

import api.ApiClient;
import model.*;
import dto.*;
import mapper.ElementMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class OsmApiService {
    private ApiClient client;
    private Gson gson;

    public OsmApiService() {
        this.client = new ApiClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ElementDto.class, new ElementDtoDeserializer())
                .create();
    }

    public List<Node> getNodesByIds(List<Long> ids) throws IOException {
        StringBuilder query = new StringBuilder("[out:json];");
        query.append("node(id:");
        for (int i = 0; i < ids.size(); i++) {
            query.append(ids.get(i));
            if (i < ids.size() - 1) {
                query.append(",");
            }
        }
        query.append(");out body;");

        String jsonResponse = client.sendQuery(query.toString());
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        List<NodeDto> nodeDtos = response.getElements().stream()
                .filter(e -> e instanceof NodeDto)
                .map(e -> (NodeDto) e)
                .toList();

        return nodeDtos.stream()
                .map(ElementMapper::map)
                .collect(Collectors.toList());
    }

    public Element getElementById(String type, long id) throws IOException {
        String query = "[out:json];" + type + "(" + id + ");out body;";
        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        if (response.getElements().isEmpty()) {
            return null;
        }

        ElementDto elementDto = response.getElements().get(0);
        switch (type){
            case "node":
                return ElementMapper.map((NodeDto) elementDto);
            case "way":
                WayDto wayDto = (WayDto) elementDto;
                List<Node> nodes = getNodesByIds(wayDto.getNodes());
                return ElementMapper.map(wayDto, nodes);
            case "relation":
                RelationDto relationDto = (RelationDto) elementDto;
                List<Element> elements = new ArrayList<>();
                for (RelationMemberDto member : relationDto.getMembers()) {
                    Element element = getElementById(member.getType(), member.getRef());
                    elements.add(element);
                }
                return ElementMapper.map(relationDto, elements);
        }

        return null;
    }


    public ArrayList<Node> getNodesByAddress(String city, String street, String housenumber) throws IOException {
        String query =  "[out:json];" +
                "node[\"addr:city\"=\"" + city + "\"][\"addr:street\"=\"" + street + "\"][\"addr:housenumber\"=\"" + housenumber + "\"];" +
                "out body;";

        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        List<NodeDto> nodeDtos = response.getElements().stream()
                .filter(e -> e instanceof NodeDto)
                .map(e -> (NodeDto) e)
                .toList();

        return nodeDtos.stream()
                .map(ElementMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Element getOSMEntityByCoordinate(String type, Double latitude, Double longitude) throws IOException{
        if (!type.equals("node") && !type.equals("relation")  && !type.equals("way")){
            throw new IllegalArgumentException("Несуществующий тип");
        }

        //Шаблон запроса
        String query =  "[out:json];" +
                type + "(around:1," + latitude + "," + longitude + ");" +
                "out body;";

        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        if (response.getElements().isEmpty()) {
            return null;
        }

        switch (type){
            case "node":
                ElementDto elementDto = response.getElements().get(0);
                return ElementMapper.map((NodeDto) elementDto);
            case "relation":
                RelationDto relationDto = (RelationDto) response.getElements().get(0);
                List<Element> elements = new ArrayList<>();

                for (RelationMemberDto member : relationDto.getMembers()) {
                    Element element = getElementById(member.getType(), member.getRef());
                    elements.add(element);
                }
                return ElementMapper.map(relationDto, elements);
            case "way":
                WayDto wayDto = (WayDto) response.getElements().get(0);
                List<Node> nodes = getNodesByIds(wayDto.getNodes());

                return ElementMapper.map(wayDto, nodes);
        }

        return null;
    }

    public ArrayList<Element> getOSMEntityByName(String type, String name) throws IOException {
        if (!type.equals("node") && !type.equals("relation")  && !type.equals("way")){
            throw new IllegalArgumentException("Несуществующий тип");
        }
        //Шаблон запроса
        String query =  "[out:json];" +
                type + "[\"name\"~\"" + name + "\"];" +
                "out body;";

        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        if (response.getElements().isEmpty()) {
            return null;
        }

        switch (type) {
            case "node":
                List<ElementDto> nodeElements = response.getElements();
                ArrayList<Element> nodeMappedElements = new ArrayList<>();
                for (ElementDto elementDto : nodeElements) {
                    nodeMappedElements.add(ElementMapper.map((NodeDto) elementDto));
                }
                return nodeMappedElements;

            case "relation":
                List<ElementDto> relationElements = response.getElements();
                ArrayList<Element> relationMappedElements = new ArrayList<>();
                for (ElementDto elementDto : relationElements) {
                    if (elementDto instanceof RelationDto) {
                        RelationDto relationDto = (RelationDto) elementDto;
                        List<Element> elements = new ArrayList<>();
                        for (RelationMemberDto member : relationDto.getMembers()) {
                            Element element = getElementById(member.getType(), member.getRef());
                            elements.add(element);
                        }
                        relationMappedElements.add(ElementMapper.map(relationDto, elements));
                    }
                }
                return relationMappedElements;

            case "way":
                List<ElementDto> wayElements = response.getElements();
                ArrayList<Element> wayMappedElements = new ArrayList<>();
                for (ElementDto elementDto : wayElements) {
                    if (elementDto instanceof WayDto) {
                        WayDto wayDto = (WayDto) elementDto;
                        List<Node> nodes = getNodesByIds(wayDto.getNodes());
                        wayMappedElements.add(ElementMapper.map(wayDto, nodes));
                    }
                }
                return wayMappedElements;
        }
        return null;
    }

    public ArrayList<Node> institutionOnStreet(String city, String street, Map<String, String> amenity) throws IOException {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("nwr[\"addr:street\"=\"").append(street).append("\"][\"addr:city\"=\"").append(city).append("\"];");

        queryBuilder.append("nwr[");
        for (Map.Entry<String, String> entry : amenity.entrySet()) {
            queryBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("][");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append("]");

        queryBuilder.append("(around:20); out geom;");
        String query = queryBuilder.toString();

        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        List<NodeDto> nodeDtos = response.getElements().stream()
                .filter(e -> e instanceof NodeDto)
                .map(e -> (NodeDto) e)
                .toList();

        return nodeDtos.stream()
                .map(ElementMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));

}

    public ArrayList<Node> publicTransportStopsOnStreet(String city, String street) throws IOException {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("nwr[\"addr:street\"=\"").append(street).append("\"][\"addr:city\"=\"").append(city).append("\"];");

        queryBuilder.append("nwr[highway=bus_stop](around:100);out geom;");

        String query = queryBuilder.toString();
        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        List<NodeDto> nodeDtos = response.getElements().stream()
                .filter(e -> e instanceof NodeDto)
                .map(e -> (NodeDto) e)
                .toList();

        return nodeDtos.stream()
                .map(ElementMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Node> publicTransportStopsRouteInTheCity(String city, int routeNumber) throws IOException {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("area[\"name\"=\"").append(city).append("\"]->.cityArea;");
        queryBuilder.append("relation[\"ref\"=\"").append(routeNumber).append("\"](area.cityArea)->.route;");

        queryBuilder.append("node(r.route)[\"highway\"=\"bus_stop\"];(._;>;);out body;");
        String query = queryBuilder.toString();

        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        List<NodeDto> nodeDtos = response.getElements().stream()
                .filter(e -> e instanceof NodeDto)
                .map(e -> (NodeDto) e)
                .toList();

        return nodeDtos.stream()
                .map(ElementMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<String> publicTransportRoutesInTheCity(String city) throws IOException {
        String query = "[out:json];" +
                "area[\"name\"=\"" + city + "\"]->.cityArea;" +
                "relation(area.cityArea)[type=route][route~\"bus|tram|trolleybus\"];" +
                "out tags;";
        String jsonResponse = client.sendQuery(query);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonResponse);

        JsonArray elementsArray = jsonElement.getAsJsonObject().getAsJsonArray("elements");
        ArrayList<String> result = new ArrayList<>();
        for (JsonElement element : elementsArray) {
            JsonObject tagsObject = element.getAsJsonObject().getAsJsonObject("tags");
            String name = tagsObject.get("name").getAsString();
            result.add(name);
        }

        return result;
    }

    public ArrayList<Coordinates> directGeocoding(String addr) throws IOException{
        String jsonResponse = client.sendQueryToNominatim("/search?q=" + addr + "&format=json&addressdetails=1");

        Type listType = new TypeToken<List<CoordinatesDto>>() {}.getType();
        List<CoordinatesDto> coordinatesList = gson.fromJson(jsonResponse, listType);

        if (coordinatesList.isEmpty()) {
            return null;
        }
        ArrayList<Coordinates> result = new ArrayList<>();

        for (CoordinatesDto coords : coordinatesList) {
            result.add(ElementMapper.map((coords)));
        }

        return result;
    }

    public Address reverseGeocoding(double lat, double lon) throws IOException{
        String jsonResponse = client.sendQueryToNominatim("/reverse?format=json&lat=" + lat + "&lon=" + lon);
        AddressDto addressDtos = gson.fromJson(jsonResponse, AddressDto.class);

        if (addressDtos == null) {
            return null;
        }

        return ElementMapper.map((addressDtos));
    }

    public List<Way> getWaysByStreet(String streetName, String cityName) throws IOException {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("way[\"addr:street\"=\"").append(streetName).append("\"][\"addr:city\"=\"").append(cityName).append("\"];out body;");

        String query = queryBuilder.toString();
        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        List<WayDto> wayDtos = response.getElements().stream()
                .filter(e -> e instanceof WayDto)
                .map(e -> (WayDto) e)
                .collect(Collectors.toList());

        List<Way> ways = new ArrayList<>();
        for (WayDto wayDto : wayDtos) {
            List<Node> nodes = getNodesByIds(wayDto.getNodes());
            Way way = ElementMapper.map(wayDto, nodes);
            ways.add(way);
        }

        return ways;
    }

    public ArrayList<Node> institutionOnCity(String city, Map<String, String> amenity) throws IOException {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("nwr[\"addr:city\"=\"").append(city).append("\"];");

        queryBuilder.append("nwr[");
        for (Map.Entry<String, String> entry : amenity.entrySet()) {
            queryBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("][");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append("]");

        queryBuilder.append("(around:20); out geom;");
        String query = queryBuilder.toString();

        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        List<NodeDto> nodeDtos = response.getElements().stream()
                .filter(e -> e instanceof NodeDto)
                .map(e -> (NodeDto) e)
                .toList();

        return nodeDtos.stream()
                .map(ElementMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));

    }
}
