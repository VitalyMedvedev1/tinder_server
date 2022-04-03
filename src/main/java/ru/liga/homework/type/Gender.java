package ru.liga.homework.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ru.liga.homework.exception.BusinessLogicException;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Gender {
    MALE("male"),
    FEMALE("female");

    private final String code;

    Gender(String code) {
        this.code = code;
    }

    @JsonCreator
    public static Gender decode(final String code) {
        return Stream.of(Gender.values())
                .filter(targetEnum -> targetEnum.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Field gender must be: " + Arrays.stream(Gender.values())
                                                                                                .map(Gender::getCode)
                                                                                                .collect(Collectors.toList())));
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}