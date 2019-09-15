package it.nfantoni.utils;

import it.nfantoni.utils.string.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

    @Test
    public void capitalizeTest(){

        assertEquals("Test", StringUtils.capitalize("test"));
    }

    @Test
    public void decapitalizeTest(){

        assertEquals("test", StringUtils.decapitalize("Test"));
    }
}
