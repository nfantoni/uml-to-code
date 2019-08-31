package it.nfantoni.utils;

import it.nfantoni.utils.settings.Settings;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Element;

import static org.junit.Assert.assertTrue;

import java.io.File;

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
}
