package ru.liga.homework.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Add_EP_Test {

    public final ConvertTextToPreRevolution convertTextToPreRevolution = new ConvertTextToPreRevolution();
    private static final char EP = 0x42A;

    @Test
    void notAddEP() {
        String str = convertTextToPreRevolution.addEP("[к]");
        assertFalse(str.contains(String.valueOf(EP)));
    }

    @Test
    void addEP() {
        String str = convertTextToPreRevolution.addEP("кот");
        assertTrue(str.contains(String.valueOf(EP)));
    }

    @Test
    void addEPinString() {
        String str = convertTextToPreRevolution.addEP("Для понимания объясним «на пальцах». Если согласная буква в разных словах означает либо мягкий, либо твёрдый звук, то звук относится к парным. Например");

        long count = Arrays
                .stream(str.split("[\\s,.:!?]+"))
                .filter(s -> s.contains(String.valueOf(EP)))
                .count();
        assertEquals(10, count);
    }
}