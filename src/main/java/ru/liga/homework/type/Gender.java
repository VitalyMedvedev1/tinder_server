package ru.liga.homework.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ru.liga.homework.exception.BusinessLogicException;

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
                .orElseThrow(() -> new BusinessLogicException("\nField gender must be!\n"));
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}