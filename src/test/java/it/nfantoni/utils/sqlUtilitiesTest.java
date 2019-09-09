package it.nfantoni.utils;

import it.nfantoni.utils.entities.Entity;
import it.nfantoni.utils.settings.Settings;
import it.nfantoni.utils.sql.SqlUtilities;
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

public class sqlUtilitiesTest {

    @Test
    public void sqlToJavaTest(){

        assertEquals("String", SqlUtilities.javaType("VARCHAR(255)"));
        assertEquals("Date", SqlUtilities.javaType("DATE"));
        assertEquals("int", SqlUtilities.javaType("INT"));
        assertEquals("long", SqlUtilities.javaType("BIGINT"));
        assertEquals("double", SqlUtilities.javaType("FLOAT"));
        assertEquals("int", SqlUtilities.javaType("INTEGER"));

    }

    @Test
    public void entityToCreateSqlTest() throws ParserConfigurationException, SAXException, IOException {

        String createSpettacoloExpected="CREATE TABLE SPETTACOLO ( CODICESPETTACOLO VARCHAR(255) NOT NULL PRIMARY KEY , NOMEARTISTA VARCHAR(255) , DATA DATE , GENERE VARCHAR(255) , NOMETEATRO VARCHAR(255) NOT NULL REFERENCES TEATRO(NOME))";
        String createTeatroExpected="CREATE TABLE TEATRO ( NOME VARCHAR(255) NOT NULL PRIMARY KEY , INDIRIZZO VARCHAR(255) , CAPIENZA INT )";
        ClassLoader classLoader = getClass().getClassLoader();
        File fXmlFile = new File(Objects.requireNonNull(classLoader.getResource("pattern_dao_spettacoli.xml")).getFile());

        List<Entity> entities = XmlUtils.readEntities(XmlUtils.readFile(fXmlFile));

        StringBuilder createTeatro=new StringBuilder();
        StringBuilder createSpettacolo = new StringBuilder();

        entities.forEach(entity -> {
            if(entity.getName().equals("Teatro"))
                createTeatro.append(SqlUtilities.sqlCreate(entity,entities));
            else
                createSpettacolo.append(SqlUtilities.sqlCreate(entity,entities));

        });

        assertEquals(createSpettacoloExpected, createSpettacolo.toString());
        assertEquals(createTeatroExpected, createTeatro.toString());
    }

    @Test
    public void entityToDropSqlTest() throws ParserConfigurationException, SAXException, IOException {

        String dropSpettacoloExpected="DROP TABLE SPETTACOLO";
        String dropTeatroExpected="DROP TABLE TEATRO";
        ClassLoader classLoader = getClass().getClassLoader();
        File fXmlFile = new File(Objects.requireNonNull(classLoader.getResource("pattern_dao_spettacoli.xml")).getFile());

        List<Entity> entities = XmlUtils.readEntities(XmlUtils.readFile(fXmlFile));

        StringBuilder dropTeatro=new StringBuilder();
        StringBuilder dropSpettacolo = new StringBuilder();

        entities.forEach(entity -> {
            if(entity.getName().equals("Teatro"))
                dropTeatro.append(SqlUtilities.sqlDrop(entity));
            else
                dropSpettacolo.append(SqlUtilities.sqlDrop(entity));

        });

        assertEquals(dropSpettacoloExpected, dropSpettacolo.toString());
        assertEquals(dropTeatroExpected, dropTeatro.toString());
    }


}