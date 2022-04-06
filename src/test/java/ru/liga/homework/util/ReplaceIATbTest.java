package ru.liga.homework.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplaceIATbTest {

    public final ConvertTextToPreRevolution convertTextToPreRevolution = new ConvertTextToPreRevolution();
    private static final char IATb = 0x463;

    @Test
    void replaceIATb() {
        String str = convertTextToPreRevolution.replaceIATb("ем");
        assertTrue(str.contains(String.valueOf(IATb)));
    }

    @Test
    void replaceIATb_1() {
        String str = convertTextToPreRevolution.replaceIATb("п1ремние hdgвеникывапр пепп престьный 1а - 66зверь78 4еее7 свирепый еда");
        assertTrue(str.contains(String.valueOf(IATb)));
        long count = Arrays
                .stream(str.split("[\\s,.:!?]+"))
                .filter(s -> s.contains(String.valueOf(IATb)))
                .count();
        assertEquals(6, count);
    }
}