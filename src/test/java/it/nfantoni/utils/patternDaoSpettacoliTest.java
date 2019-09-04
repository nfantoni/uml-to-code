package it.nfantoni.utils;

import it.nfantoni.utils.entities.Entity;
import it.nfantoni.utils.settings.Settings;
import it.nfantoni.utils.xml.XmlUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class patternDaoSpettacoliTest {

	@Test
	public void ReadSettingsTest() throws Exception{

		ClassLoader classLoader = getClass().getClassLoader();

		File fXmlFile = new File(Objects.requireNonNull(classLoader.getResource("pattern_dao_spettacoli.xml")).getFile());

		Settings settings = XmlUtils.readSetting(XmlUtils.readFile(fXmlFile));

        assertEquals("it.nfantoni.dao.test", settings.getDefaultPackage());
        assertEquals("build/dao",settings.getOutputPath());
		assertSame(Settings.OUTPUT_TYPE.DAO,settings.getOutputType());
		assertTrue(!settings.getUseSurrogatesId());
	
	}

	@Test
	public void ReadEntityTest() throws Exception{

		ClassLoader classLoader = getClass().getClassLoader();
		File fXmlFile = new File(Objects.requireNonNull(classLoader.getResource("pattern_dao_spettacoli.xml")).getFile());

        List<Entity> entities = XmlUtils.readEntities(XmlUtils.readFile(fXmlFile));

        assertEquals(2, entities.size());
        assertEquals(3, entities.get(0).getAttributes().size());
        assertEquals(4, entities.get(1).getAttributes().size());
        assertEquals(1, entities.get(0).getAssociations().size());
        assertEquals(1, entities.get(1).getAssociations().size());

        assertEquals("Teatro", entities.get(0).getName());
        assertEquals("Nome", entities.get(0).getAttributes().get(0).getName());
        assertEquals("VARCHAR(255)", entities.get(0).getAttributes().get(0).getSqlType());
        assertTrue(!entities.get(0).getAttributes().get(0).getNull());
        assertTrue(entities.get(0).getAttributes().get(0).getPrimaryKey());

        assertEquals("Indirizzo", entities.get(0).getAttributes().get(1).getName());
        assertEquals("VARCHAR(255)", entities.get(0).getAttributes().get(1).getSqlType());
        assertTrue(entities.get(0).getAttributes().get(1).getNull());
        assertTrue(!entities.get(0).getAttributes().get(1).getPrimaryKey());

        assertEquals("Capienza", entities.get(0).getAttributes().get(2).getName());
        assertEquals("INT", entities.get(0).getAttributes().get(2).getSqlType());
        assertTrue(entities.get(0).getAttributes().get(2).getNull());
        assertTrue(!entities.get(0).getAttributes().get(2).getPrimaryKey());

        assertEquals("Spettacolo", entities.get(1).getName());
        assertEquals("CodiceSpettacolo", entities.get(1).getAttributes().get(0).getName());
        assertEquals("VARCHAR(255)", entities.get(1).getAttributes().get(0).getSqlType());
        assertTrue(!entities.get(1).getAttributes().get(0).getNull());
        assertTrue(entities.get(1).getAttributes().get(0).getPrimaryKey());

        assertEquals("NomeArtista", entities.get(1).getAttributes().get(1).getName());
        assertEquals("VARCHAR(255)", entities.get(1).getAttributes().get(1).getSqlType());
        assertTrue(entities.get(1).getAttributes().get(1).getNull());
        assertTrue(!entities.get(1).getAttributes().get(1).getPrimaryKey());

        assertEquals("Data", entities.get(1).getAttributes().get(2).getName());
        assertEquals("DATE", entities.get(1).getAttributes().get(2).getSqlType());
        assertTrue(entities.get(1).getAttributes().get(2).getNull());
        assertTrue(!entities.get(1).getAttributes().get(2).getPrimaryKey());

        assertEquals("Genere", entities.get(1).getAttributes().get(3).getName());
        assertEquals("VARCHAR(255)", entities.get(1).getAttributes().get(3).getSqlType());
        assertTrue(entities.get(1).getAttributes().get(3).getNull());
        assertTrue(!entities.get(1).getAttributes().get(3).getPrimaryKey());

        assertEquals("Spettacolo", entities.get(0).getAssociations().get(0).getClassName());
        assertEquals("*", entities.get(0).getAssociations().get(0).getMultiplicity());
        assertEquals("Teatro", entities.get(1).getAssociations().get(0).getClassName());
        assertEquals("1", entities.get(1).getAssociations().get(0).getMultiplicity());
	}

}
