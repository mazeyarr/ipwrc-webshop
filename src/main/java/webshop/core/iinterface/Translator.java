package webshop.core.iinterface;

import webshop.core.service.TranslateService;
import webshop.type.LanguageCodeType;

public abstract class Translator {
    public static TranslateService getTranslator() {
        return TranslateService.getInstance();
    }

    public static String translate(String text) {
        return getTranslator().translate(text);
    }

    public static String translate(String text, LanguageCodeType from, LanguageCodeType to) {
        return getTranslator().translate(text, from, to);
    }
}
