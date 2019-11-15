package com.baselib.language;

public enum LanguageType {

    CHINESE("ch"),
    ENGLISH("en"),
    MARATHI("mr"),
    HINDI("hi"),
    PUNJABI("pa"),
    GUJARATI("gu"),
    TAMIL("ta"),
    TELUGU("te"),
    KANNADA("kn"),
    MALAYALAM("ml"),
    ARABIC("ar");

    private String language;

    LanguageType(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language == null ? "" : language;
    }
}
