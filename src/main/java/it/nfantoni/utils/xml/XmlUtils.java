package it.nfantoni.utils.xml;

import it.nfantoni.utils.settings.Settings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import it.nfantoni.utils.entities.Entity;

public class XmlUtils {
    private  XmlUtils() {
    }

    public static Document readFile(File fXmlFile) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();
        return doc;
    }

    public static Settings readSetting(Document document){
        NodeList nList = document.getElementsByTagName("settings");
        return new Settings((Element) nList.item(0));
    }

    public static List<Entity> readEntities(Document document){
        NodeList nList = document.getElementsByTagName("entity");

        List<Entity> entities = new ArrayList<>();

        IntStream.range(0, nList.getLength())
                .mapToObj(nList::item)
                .forEach(item ->
                        entities.add(new Entity((Element)item)));

        return entities;
    }
}
