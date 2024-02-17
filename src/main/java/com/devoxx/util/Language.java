package com.devoxx.util;

/**
 * Language enum
 */
public enum Language {

    ENGLISH("en"),
    FRENCH("fr"),
    SPANISH("es"),
    GERMAN("de"),
    ITALIAN("it"),
    DUTCH("nl");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
