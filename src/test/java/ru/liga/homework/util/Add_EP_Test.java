package ru.liga.homework.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Add_EP_Test {

    public final ConvertTextToPreRevolution convertTextToPreRevolution = new ConvertTextToPreRevolution();
    private static final char c = 0x42A;

    @Test
    void addEP1() {
        String str = convertTextToPreRevolution.convert("[к]");
        assertFalse(str.contains(String.valueOf(c)));
    }

    @Test
    void addEP2() {
        String str = convertTextToPreRevolution.convert("кот");
        assertTrue(str.contains(String.valueOf(c)));
    }

    @Test
    void addEP3() {
        String str = convertTextToPreRevolution.convert("Для понимания объясним «на пальцах». Если согласная буква в разных словах означает либо мягкий, либо твёрдый звук, то звук относится к парным. Например");

        long count = Arrays
                .stream(str.split("[\\s,.:!?]+"))
                .filter(s -> s.contains(String.valueOf(c)))
                .count();
        assertEquals(10, count);
    }
}