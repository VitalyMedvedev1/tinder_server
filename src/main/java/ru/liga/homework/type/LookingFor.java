package ru.liga.homework.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ru.liga.homework.exception.BusinessLogicException;

import java.util.Arrays;
import java.util.stream.Stream;

public enum LookingFor {
    MALES("males"),
    FEMALES("females"),
    ALL("all");

    private final String code;

    LookingFor(String code) {
        this.code = code;
    }

    @JsonCreator
    public static LookingFor decode(final String code) {
        return Stream.of(LookingFor.values())
                .filter(targetEnum -> targetEnum.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Field must be: " + Arrays.toString(LookingFor.values())));
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}