package it.nfantoni.utils;

import it.nfantoni.utils.entities.Entity;
import it.nfantoni.utils.settings.Settings;
import it.nfantoni.utils.worker.Dao;
import it.nfantoni.utils.worker.Worker;
import it.nfantoni.utils.xml.XmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        String param = args[0];

        switch (param) {
            case "-h":
                System.out.println("### UML TO CODE");
                System.out.println("usage");
                System.out.println("java -jar uml-to-code-1.0.0-SNAPSHOT.jar -f xml-file-path");
                break;
            case "-f":
                File xml = new File(args[1]);

                Document document = XmlUtils.readFile(xml);
                Settings settings = XmlUtils.readSetting(document);
                List<Entity> entities = XmlUtils.readEntities(document);

                switch (settings.getOutputType()){
                    case DAO:
                        Worker worker = new Dao();
                        worker.work(settings,entities);
                        break;
                }

                break;
            default:
                System.out.println("### UML TO CODE");
                System.out.println("usage");
                System.out.println("java -jar uml-to-code-1.0.0-SNAPSHOT.jar -f xml-file.xml");


        }

        System.out.println("Finish !");
    }
}
