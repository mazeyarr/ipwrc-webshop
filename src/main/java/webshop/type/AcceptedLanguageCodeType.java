package webshop.type;

import webshop.core.exception.EnumValueNotFound;

public enum AcceptedLanguageCodeType {
    english("en"),
    dutch("nl"),
    spanish("es"),
    french("fr"),
    german("de"),
    chinese("zh");

    public String acceptedLanguage;

    AcceptedLanguageCodeType(String s) {
        this.acceptedLanguage = s;
    }

    public static AcceptedLanguageCodeType valueOfLanguage(String s) throws EnumValueNotFound {
        for (AcceptedLanguageCodeType language : values()) {
            if (s.equals(language.acceptedLanguage)) {
                return language;
            }
        }

        throw new EnumValueNotFound(
                s + " is not a enum value of " + AcceptedLanguageCodeType.class.getName()
        );
    }
}
