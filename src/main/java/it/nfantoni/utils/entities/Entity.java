package it.nfantoni.utils.entities;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Entity {
    private String name;
    private List<Attributes> attributes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attributes> attributes) {
        this.attributes = attributes;
    }

    public Entity(Element element) {
        this.name = element.getAttribute("name");

        NodeList attributeNodeList = element.getElementsByTagName("attribute");

        Stream<Node> nodeStream = IntStream.range(0, attributeNodeList.getLength())
                .mapToObj(attributeNodeList::item);

        nodeStream.forEach(item -> {
            String isNull = ((Element)item).getAttribute("null");
            String isPrimaryKey = ((Element)item).getAttribute("primaryKey");

            attributes.add(new Attributes(((Element)item).getAttribute("name"),
                                            ((Element)item).getAttribute("sqlType"),
                                            Boolean.parseBoolean((isNull == null || isNull.isEmpty())?"true":isNull),
                                            Boolean.parseBoolean((isPrimaryKey == null || isPrimaryKey.isEmpty())?"false":isPrimaryKey)));
        });

    }
}