package ru.liga.homework.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Add_i_decimalTest {

    public final ConvertTextToPreRevolution convertTextToPreRevolution = new ConvertTextToPreRevolution();

    @Test
    void add_i_december() {
        convertTextToPreRevolution.add_i_december("В единственной строке записан текст. Для каждого слова из данного текста подсчитайте, сколько раз оно встречалось в этом тексте ранее.\n" +
                "Словом считается последовательность непробельных символов идущих подряд, слова разделены одним или большим числом пробелов или символами конца строки.");
    }
}