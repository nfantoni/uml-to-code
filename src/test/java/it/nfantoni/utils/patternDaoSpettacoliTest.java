package it.nfantoni.utils;

import it.nfantoni.utils.entities.Entity;
import it.nfantoni.utils.settings.Settings;
import it.nfantoni.utils.worker.Dao;
import it.nfantoni.utils.xml.XmlUtils;
import org.apache.commons.io.FileDeleteStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FileUtils;

import static org.junit.Assert.*;

public class patternDaoSpettacoliTest {

    @After
    public void afterTest() throws IOException {
        File file = new File("target/dao");
        FileDeleteStrategy.FORCE.delete(file);
    }

    @Before
    public void beforeTest() throws IOException {
        File file = new File("target/dao");
        FileDeleteStrategy.FORCE.delete(file);
    }

	@Test
	public void ReadSettingsTest() throws Exception{

		ClassLoader classLoader = getClass().getClassLoader();

		File fXmlFile = new File(Objects.requireNonNull(classLoader.getResource("pattern_dao_spettacoli.xml")).getFile());

		Settings settings = XmlUtils.readSetting(XmlUtils.readFile(fXmlFile));

        assertEquals("it.nfantoni.dao.test", settings.getDefaultPackage());
        assertEquals("target/dao",settings.getOutputPath());
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
        assertEquals("1", entities.get(0).getAssociations().get(0).getMultiplicity());
        assertEquals("Teatro", entities.get(1).getAssociations().get(0).getClassName());
        assertEquals("*", entities.get(1).getAssociations().get(0).getMultiplicity());
	}

	@Test
    public void daoPatternFolderTest() throws ParserConfigurationException, SAXException, IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File fXmlFile = new File(Objects.requireNonNull(classLoader.getResource("pattern_dao_spettacoli.xml")).getFile());

        List<Entity> entities = XmlUtils.readEntities(XmlUtils.readFile(fXmlFile));
        Settings settings = XmlUtils.readSetting(XmlUtils.readFile(fXmlFile));

        Dao dao = new Dao();
        dao.work(settings,entities);

        assertTrue(new File("target/dao").exists());
        assertTrue(new File("target/dao/src/dao").exists());
        assertTrue(new File("target/dao/src/dao/db2").exists());

        assertTrue(new File("target/dao/src/dao/db2/Db2DAOFactory.java").exists());
        assertTrue(new File("target/dao/src/dao/db2/Db2SpettacoloDAO.java").exists());
        assertTrue(new File("target/dao/src/dao/db2/Db2TeatroDAO.java").exists());

        assertTrue(new File("target/dao/src/dao/DAOFactory.java").exists());
        assertTrue(new File("target/dao/src/dao/DAOTest.java").exists());
        assertTrue(new File("target/dao/src/dao/SpettacoloDAO.java").exists());
        assertTrue(new File("target/dao/src/dao/SpettacoloDTO.java").exists());
        assertTrue(new File("target/dao/src/dao/TeatroDAO.java").exists());
        assertTrue(new File("target/dao/src/dao/TeatroDTO.java").exists());
    }

    @Test
    public void daoPatternFileTest() throws ParserConfigurationException, SAXException, IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File fXmlFile = new File(Objects.requireNonNull(classLoader.getResource("pattern_dao_spettacoli.xml")).getFile());

        List<Entity> entities = XmlUtils.readEntities(XmlUtils.readFile(fXmlFile));
        Settings settings = XmlUtils.readSetting(XmlUtils.readFile(fXmlFile));

        Dao dao = new Dao();
        dao.work(settings,entities);

        assertTrue(checkContent("expected/patter-dao-spettacoli/src/dao/DAOFactory.java",
                "target/dao/src/dao/DAOFactory.java"));
        assertTrue(checkContent("expected/patter-dao-spettacoli/src/dao/SpettacoloDAO.java",
                "target/dao/src/dao/SpettacoloDAO.java"));
        assertTrue(checkContent("expected/patter-dao-spettacoli/src/dao/TeatroDAO.java",
                "target/dao/src/dao/TeatroDAO.java"));

        assertTrue(checkContent("expected/patter-dao-spettacoli/src/dao/TeatroDTO.java",
                "target/dao/src/dao/TeatroDTO.java"));
        assertTrue(checkContent("expected/patter-dao-spettacoli/src/dao/SpettacoloDTO.java",
                "target/dao/src/dao/SpettacoloDTO.java"));

        assertTrue(checkContent("expected/patter-dao-spettacoli/src/dao/db2/Db2DAOFactory.java",
                "target/dao/src/dao/db2/Db2DAOFactory.java"));

        assertTrue(checkContent("expected/patter-dao-spettacoli/src/dao/db2/Db2SpettacoloDAO.java",
                "target/dao/src/dao/db2/Db2SpettacoloDAO.java"));
        assertTrue(checkContent("expected/patter-dao-spettacoli/src/dao/db2/Db2TeatroDAO.java",
                "target/dao/src/dao/db2/Db2TeatroDAO.java"));

    }

    private Boolean checkContent(String pathExpected, String pathActual) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File expected =  new File(Objects.requireNonNull(classLoader.getResource(pathExpected)).getFile());
        File actual = new File(pathActual);

        String expectedString = new String(Files.readAllBytes(Paths.get(expected.getAbsolutePath())))
                .replace("\n", "").replace("\r", "");
        String actualString = new String(Files.readAllBytes( Paths.get(actual.getAbsolutePath())))
                .replace("\n", "").replace("\r", "");
        return expectedString.equals(actualString);

    }
}
