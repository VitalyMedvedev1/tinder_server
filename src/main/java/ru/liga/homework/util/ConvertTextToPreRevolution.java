package ru.liga.homework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ConvertTextToPreRevolution {

    private static final String REG_EXP_STRING = "[\\s,.:!?]+";
    private static final char EP = 0x44A;
    private static final String CONSONANTS_PATTERN = "[бпвфдтзсжшчцщгкхмнлрБПВФДТЗСЖШЧЦЩГКХМНЛР]$";
    private static final char I_DECIMAL = 0x456;
    private static final String I_DECIMAL_PATTERN = "[иИ]+[ауоыэяюёиеАУОЫЭЯЮЁИЕйЙ]";
    private static final char qp = 'Ф';
    private static final char FITA = 0x472;
    private static final char IATb = 0x463;
    private static final char e = 'е';
    private static final String SPACE = " ";
    private static final int INDX_FIRST = 0;
    private static final int INDX_LAST = 1;

    public String convert(String text) {
        log.info("Start convert text {} to pre-revolution text", text);
        return addEP(replace_i_decimal(replaceFITAinUserName(replaceIATb(text))));
    }

    protected String addEP(String text) {
        log.debug("Convert text {}, add symbol {}", text, EP);
        List<String> listText = new ArrayList<>(Arrays.asList(text.split(REG_EXP_STRING)));
        Pattern pattern = Pattern.compile(CONSONANTS_PATTERN);

        return listText.stream()
                .map(s -> {
                    if (pattern.matcher(s).find()) {
                        s += EP;
                    }
                    return s;
                }).collect(Collectors.joining(SPACE));
    }

    protected String replace_i_decimal(String text) {
        log.debug("Convert text {}, replace 'и' on symbol {}", text, I_DECIMAL);
        List<String> listText = new ArrayList<>(Arrays.asList(text.split(REG_EXP_STRING)));
        Pattern pattern = Pattern.compile(I_DECIMAL_PATTERN);

        return listText.stream()
                .map(s -> {
                    Matcher matcher = pattern.matcher(s);
                    while (matcher.find()) {
                        s = s.substring(INDX_FIRST, matcher.start()) + I_DECIMAL + s.substring(matcher.start() + INDX_LAST);
                    }
                    return s;
                }).collect(Collectors.joining(SPACE));
    }

    private final HashSet<String> hashReplaceName = new HashSet<>(Arrays.asList(
            "АГАФЬЯ", "АНФИМ", "АФАНАСИЙ", "АФИНА", "ВАРФОЛОМЕЙ",
            "ГОЛИАФ", "ЕВФИМИЙ", "МАРФА", "МАТФЕЙ", "МЕФОДИЙ",
            "НАФАНАИЛ", "ПАРФЕНОН", "ПИФАГОР", "РУФЬ", "САВАОФ",
            "ТИМОФЕЙ", "ЭСФИРЬ", "ИУДИФЬ", "ФАДДЕЙ", "ФЕКЛА", "ФЕМИДА",
            "ФЕМИСТОКЛ", "ФЕОДОР", "ФЁДОР", "ФЕДЯ", "ФЕОДОСИЙ",
            "ФЕДОСИЙ", "ФЕОДОСИЯ", "ФЕОДОТ", "ФЕДОТ", "ФЕОФАН",
            "ФЕОФИЛ", "ФЕРАПОНТ", "ФОМА", "ФОМИНИЧНА"
    ));

    protected String replaceFITAinUserName(String text) {
        log.debug("Convert text(name) {}, replace 'qp' on symbol {}", text, FITA);
        List<String> listText = new ArrayList<>(Arrays.asList(text.toUpperCase().split(REG_EXP_STRING)));
        return listText.stream()
                .map(s -> {
                    if (hashReplaceName.contains(s.toUpperCase())) {
                        int position = s.toUpperCase().indexOf(qp);
                        s = s.substring(INDX_FIRST, position) + FITA + s.substring(position + INDX_LAST);
                    }
                    return s.substring(INDX_FIRST, INDX_LAST).toUpperCase() + s.substring(INDX_LAST).toLowerCase();
                }).collect(Collectors.joining(SPACE));
    }

    private final HashSet<String> hashReplaceIATb = new HashSet<>(Arrays.asList(
            "еда", "ем", "есть", "обед", "обедня", "сыроежка", "сыроега", "медведь", "снедь", "едкий", "ехать", "езда", "уезд", "еду", "ездить", "поезд",
            "бег", "беглец", "беженец", "забегаловка", "избегать", "избежать", "набег", "перебежчик", "пробег", "разбег", "убежище", "центробежнаясила",
            "беда", "бедный", "победить", "убедить", "убеждение", "белый", "бельё", "белка", "бельмо", "белуга", "бес", "бешеный", "обет", "обещать", "веять",
            "веер", "ветер", "ветвь", "веха", "ведать", "веди", "весть", "повесть", "ве́дение", "вежливый", "невежда", "вежды", "вежа", "век", "вечный", "увечить",
            "веко", "венок", "венец", "вениквенок", "венец", "веник", "вено", "вера", "вероятно", "суеверие", "вес", "вешать", "повеса", "равновесие", "звезда",
            "зверь", "невеста", "ответ", "совет", "привет", "завет", "вещать", "вече", "свежий", "свежеть", "свет", "свеча", "просвещение", "светец", "светёлка",
            "Светлана", "цвет", "цветы", "цвести", "человек", "человеческий", "деть", "девать", "одеть", "одевать", "одеяло", "одеяние", "дело", "делать",
            "действие", "неделя", "надеяться", "свидетель", "дева", "девочка", "дед", "делить", "предел", "дети", "детёныш", "детка", "детство", "зевать",
            "зев", "ротозей", "зело", "зеница", "зенки", "левый", "левша", "лезть", "лестница", "облезлый", "лекарь", "лечить", "лекарство", "лень", "ленивец",
            "ленивый", "лентяй", "лепота", "великолепный", "лепить", "нелепый", "слепок", "лес", "лесник", "лесничий", "лесопилка", "леший", "лето", "долголетие",
            "Летов", "летоисчисление", "летописец", "летопись", "малолетка", "однолетка", "пятилетка", "совершеннолетие", "леха́", "бледный", "железо", "железняк",
            "калека", "калечить", "клеть", "клетка", "колено", "наколенник", "поколение", "лелеять", "млеть", "плен", "пленённый", "пленить", "пленник", "плесень",
            "плешь", "Плеханов", "полено", "след", "последователь", "последствие", "преследовать", "следить", "следопыт", "слепой", "телега", "тележный", "тлен",
            "тление", "тленный", "хлеб", "хлев", "медь", "медный", "мел", "менять", "изменник", "непременно", "мера", "намерение", "лицемер", "месяц", "месить", "мешать",
            "помеха", "место", "мещанин", "помещик", "наместник", "метить", "замечать", "примечание", "сметить", "смета", "мех", "мешок", "змей", "змея", "сметь", "смелый",
            "смеяться", "смех", "нега", "нежный", "нежить", "недра", "внедрить", "немой", "немец", "нет", "отнекаться", "гнев", "гнедой", "гнездо", "загнетка", "снег",
            "снежный", "мнение", "сомнение", "сомневаться", "петь", "песня", "петух", "пегий", "пена", "пенязь", "пестовать", "пестун", "пехота", "пеший", "опешить",
            "спеть", "спех", "спешить", "успех", "реять", "река", "речь", "наречие", "редкий", "редька", "резать", "резвый", "репа", "репица", "ресница", "обретать",
            "обрести", "сретение", "встречать", "прореха", "решето", "решётка", "решать", "решить", "грех", "грешный", "зреть", "созреть", "зрелый", "зрение", "крепкий",
            "крепиться", "орех", "преть", "прелый", "прение", "пресный", "свирепый", "свирель", "стрела", "стрелять", "стреха", "застреха", "хрен", "сусек", "сеять", "семя",
            "север", "седло", "сесть", "беседа", "сосед", "седой", "седеть", "секу", "сечь", "сеча", "сечение", "просека", "насекомое", "сень", "осенять", "сени", "сено",
            "серый", "сера", "посетить", "посещать", "сетовать", "сеть", "сетка", "стена", "застенок", "застенчивый", "стенгазета", "тело", "мягкотелость", "растелешиться",
            "тельняшка", "тень", "оттенок", "тенёк", "тесто", "тесный", "стеснять", "стесняться", "теснить", "тесниться", "затеять", "затея", "утеха", "потеха", "тешить", "утешение",
            "хер", "похерить", "цевка", "цевье", "цевница", "цедить", "целый", "исцелять", "целовать", "поцелуй", "цель", "целиться", "цена", "цепь", "цеплять", "цеп"
    ));

    protected String replaceIATb(String text) {
        log.debug("Convert text {}, replace e on symbol {}", text, IATb);
        List<String> listText = new ArrayList<>(Arrays.asList(text.split(REG_EXP_STRING)));
        return listText.stream()
                .map(s -> {
                    if (hashReplaceIATb.contains(s)) {
                        int position = s.indexOf(e);
                        s = s.substring(INDX_FIRST, position) + IATb + s.substring(position + INDX_LAST);
                    }
                    return s;
                }).collect(Collectors.joining(SPACE));
    }
}