package ru.liga.homework.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReplaceFitaTest {

    public final ConvertTextToPreRevolution convertTextToPreRevolution = new ConvertTextToPreRevolution();
    private static final char FITA = 0x472;

    @Test
    void replaceFITAinUserName() {
        String repName = convertTextToPreRevolution.replaceFITAinUserName("Варфоломей");
        assertTrue(repName.toUpperCase().contains(String.valueOf(FITA)));
    }

    @Test
    void replaceFITAinUserName_1() {
        String repName = convertTextToPreRevolution.replaceFITAinUserName("Фома фома гггг фома");
        assertTrue(repName.contains(String.valueOf(FITA)));
    }

    @Test
    void notReplaceFITAinUserName_1() {
        String repName = convertTextToPreRevolution.replaceFITAinUserName("ФЕНОКЕНТИЙ");
        assertFalse(repName.contains(String.valueOf(FITA)));
    }

    @Test
    void notReplaceFITAinUserName_2() {
        String repName = convertTextToPreRevolution.replaceFITAinUserName("Обьездил весь свет");
        assertFalse(repName.contains(String.valueOf(FITA)));
    }
}