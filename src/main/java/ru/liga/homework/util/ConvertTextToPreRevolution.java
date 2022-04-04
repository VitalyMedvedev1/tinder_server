package ru.liga.homework.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ConvertTextToPreRevolution {

    private static final char c = 0x42A;
    private static final String REG_EXP_STRING = "[\\s,.:!?]+";
    private static final String CONSONANTS_PATTERN = "[бпвфдтзсжшчцщгкхмнлрБПВФДТЗСЖШЧЦЩГКХМНЛР]$";
    private static final String I_DECIMAL_PATTERN = "[бпвфдтзсжшчцщгкхмнлрБПВФДТЗСЖШЧЦЩГКХМНЛР]$";

    public String convert(String text){

        text = addEP(text);

        return text;
    }

    public String addEP(String text) {

        List<String> listText = new ArrayList<>(Arrays.asList(text.split(REG_EXP_STRING)));
        Pattern pattern = Pattern.compile(CONSONANTS_PATTERN);
        String str = listText.stream().map(s -> {
            if (pattern.matcher(s).find()) {
                s += c;
            }
            return s;
        }).collect(Collectors.joining(" "));

        return str;
    }

    public String add_i_december(String text){
/*
        List<String> listText = new ArrayList<>(Arrays.asList(text.split(REG_EXP_STRING)));
        Pattern pattern = Pattern.compile(I_DECIMAL_PATTERN);
        listText.stream().map(s -> {
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()){
                s.re
            }
        });
*/

        return null;
    }
}
