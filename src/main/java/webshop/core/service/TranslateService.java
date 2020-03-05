package webshop.core.service;

import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import webshop.core.service.Amazon.AmazonTranslationService;
import webshop.type.LanguageCodeType;

public class TranslateService {
    private static volatile TranslateService TRANSLATE_SERVICE_INSTANCE;

    private AmazonTranslationService amazonTranslationService;
    private String text;

    public TranslateService() {
        amazonTranslationService = new AmazonTranslationService();
    }

    public String translate(String text) {
        return this.setText(text).translateRequest(LanguageCodeType.en, LanguageCodeType.nl);
    }

    public String translate(String text, LanguageCodeType from, LanguageCodeType to) {
        return this.setText(text).translateRequest(from, to);
    }

    public String translate() {
        return translateRequest(LanguageCodeType.en, LanguageCodeType.nl);
    }

    public String translate(LanguageCodeType from, LanguageCodeType to) {
        return translateRequest(from, to);
    }

    public String translate(LanguageCodeType to) {
        return translateRequest(LanguageCodeType.en, to);
    }

    private String translateRequest(LanguageCodeType sourceLanguageCode,
                                    LanguageCodeType targetLanguageCode) {
        if (!CoreService.isTranslationServiceOn()) {
            return this.getText();
        }

        TranslateTextRequest request = new TranslateTextRequest()
                .withText(this.getText())
                .withSourceLanguageCode(sourceLanguageCode.toString())
                .withTargetLanguageCode(targetLanguageCode.toString());

        TranslateTextResult result  = getAmazonTranslationService()
                .getTranslator()
                .translateText(request);

        return result.getTranslatedText();
    }

    public AmazonTranslationService getAmazonTranslationService() {
        return amazonTranslationService;
    }

    public String getText() {
        return text;
    }

    public TranslateService setText(String text) {
        this.text = text;

        return this;
    }

    public static TranslateService getInstance() {
        if (TRANSLATE_SERVICE_INSTANCE == null) {
            synchronized (TranslateService.class) {
                if (TRANSLATE_SERVICE_INSTANCE == null) {
                    TRANSLATE_SERVICE_INSTANCE = new TranslateService();
                }
            }
        }

        TRANSLATE_SERVICE_INSTANCE.setText("");

        return TRANSLATE_SERVICE_INSTANCE;
    }
}
