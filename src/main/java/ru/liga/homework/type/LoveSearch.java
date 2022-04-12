package ru.liga.homework.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ru.liga.homework.exception.BusinessLogicException;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LoveSearch {
    MALES("males"),
    FEMALES("females"),
    ALL("all");

    private final String code;

    LoveSearch(String code) {
        this.code = code;
    }

    @JsonCreator
    public static LoveSearch decode(final String code) {
        return Stream.of(LoveSearch.values())
                .filter(targetEnum -> targetEnum.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Field lookingfor must be: " + Arrays.stream(LoveSearch.values())
                                                                                                        .map(LoveSearch::getCode)
                                                                                                        .collect(Collectors.toList())));
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}