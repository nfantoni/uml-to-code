package it.nfantoni.utils;

import it.nfantoni.utils.entities.Entity;
import it.nfantoni.utils.settings.Settings;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Element;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class patternDaoSpettacoliTest {

	@Test
	public void ReadSettingsTest() throws Exception{

		ClassLoader classLoader = getClass().getClassLoader();
		File fXmlFile = new File(classLoader.getResource("pattern_dao_spettacoli.xml").getFile());
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("settings");
		
		Settings settings = new Settings((Element) nList.item(0));

        assertTrue("it.nfantoni.dao.test".equals( settings.getDefaultPackage()));
		assertTrue("build/dao".equals(settings.getOutputPath()));
		assertTrue(settings.getOutputType()== Settings.OUTPUT_TYPE.DAO);
		assertTrue(settings.getUseSurrogatesId()==false);
	
	}

	@Test
	public void ReadEntityTest() throws Exception{

		ClassLoader classLoader = getClass().getClassLoader();
		File fXmlFile = new File(classLoader.getResource("pattern_dao_spettacoli.xml").getFile());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("entity");

        List<Entity> entities = new ArrayList<>();

        for(int i=0; i <= nList.getLength()-1; i++)
        {
            Element element = (Element) nList.item(i);
            if (element.getParentNode().getNodeName().equals("entities")){
                Entity entity = new Entity((Element) nList.item(i));
                entities.add(entity);
            }

        }

        assertTrue(entities.size()==2);
        assertTrue(entities.get(0).getAttributes().size()==3);
        assertTrue(entities.get(1).getAttributes().size()==4);
        assertTrue(entities.get(0).getAssociations().size()==1);
        assertTrue(entities.get(1).getAssociations().size()==1);

        assertTrue(entities.get(0).getName().equals("Teatro"));
        assertTrue(entities.get(0).getAttributes().get(0).getName().equals("Nome"));
        assertTrue(entities.get(0).getAttributes().get(0).getSqlType().equals("VARCHAR(255)"));
        assertTrue(entities.get(0).getAttributes().get(0).getNull() == false);
        assertTrue(entities.get(0).getAttributes().get(0).getPrimaryKey() == true);

        assertTrue(entities.get(0).getAttributes().get(1).getName().equals("Indirizzo"));
        assertTrue(entities.get(0).getAttributes().get(1).getSqlType().equals("VARCHAR(255)"));
        assertTrue(entities.get(0).getAttributes().get(1).getNull() == true);
        assertTrue(entities.get(0).getAttributes().get(1).getPrimaryKey() == false);

        assertTrue(entities.get(0).getAttributes().get(2).getName().equals("Capienza"));
        assertTrue(entities.get(0).getAttributes().get(2).getSqlType().equals("INT"));
        assertTrue(entities.get(0).getAttributes().get(2).getNull() == true);
        assertTrue(entities.get(0).getAttributes().get(2).getPrimaryKey() == false);

        assertTrue(entities.get(1).getName().equals("Spettacolo"));
        assertTrue(entities.get(1).getAttributes().get(0).getName().equals("CodiceSpettacolo"));
        assertTrue(entities.get(1).getAttributes().get(0).getSqlType().equals("VARCHAR(255)"));
        assertTrue(entities.get(1).getAttributes().get(0).getNull() == false);
        assertTrue(entities.get(1).getAttributes().get(0).getPrimaryKey() == true);

        assertTrue(entities.get(1).getAttributes().get(1).getName().equals("NomeArtista"));
        assertTrue(entities.get(1).getAttributes().get(1).getSqlType().equals("VARCHAR(255)"));
        assertTrue(entities.get(1).getAttributes().get(1).getNull() == true);
        assertTrue(entities.get(1).getAttributes().get(1).getPrimaryKey() == false);

        assertTrue(entities.get(1).getAttributes().get(2).getName().equals("Data"));
        assertTrue(entities.get(1).getAttributes().get(2).getSqlType().equals("DATE"));
        assertTrue(entities.get(1).getAttributes().get(2).getNull() == true);
        assertTrue(entities.get(1).getAttributes().get(2).getPrimaryKey() == false);

        assertTrue(entities.get(1).getAttributes().get(3).getName().equals("Genere"));
        assertTrue(entities.get(1).getAttributes().get(3).getSqlType().equals("VARCHAR(255)"));
        assertTrue(entities.get(1).getAttributes().get(3).getNull() == true);
        assertTrue(entities.get(1).getAttributes().get(3).getPrimaryKey() == false);

        assertTrue(entities.get(0).getAssociations().get(0).getClassName().equals("Spettacolo"));
        assertTrue(entities.get(0).getAssociations().get(0).getMultiplicity().equals("*"));
        assertTrue(entities.get(1).getAssociations().get(0).getClassName().equals("Teatro"));
        assertTrue(entities.get(1).getAssociations().get(0).getMultiplicity().equals("1"));
	}

}
