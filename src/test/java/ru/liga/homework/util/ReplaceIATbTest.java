package ru.liga.homework.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReplaceIATbTest {

    public final ConvertTextToPreRevolution convertTextToPreRevolution = new ConvertTextToPreRevolution();
    private static final char IATb = 0x463;

    @Test
    void replaceIATb() {
        String repName = convertTextToPreRevolution.replaceIATb("Светлана");
        assertTrue(repName.contains(String.valueOf(IATb)));
    }

    @Test
    void replaceIATb_1() {
        String repName = convertTextToPreRevolution.replaceIATb("скверсевернос");
        assertTrue(repName.contains(String.valueOf(IATb)));
    }
}