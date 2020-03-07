package webshop.interceptor;

import webshop.module.User.service.AuthUserService;
import webshop.type.AcceptedLanguageCodeType;
import webshop.type.LanguageCodeType;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LocaleInterceptor implements ReaderInterceptor {
    private final static int FIRST_ELEMENT_INDEX = 0;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        try {
            String acceptLanguage = context.getHeaders().getFirst("Accept-Language");
            List<Locale.LanguageRange> locales = Locale.LanguageRange.parse(acceptLanguage);

            if (locales.size() > FIRST_ELEMENT_INDEX) {
                locales.sort(Comparator.comparingDouble(Locale.LanguageRange::getWeight));
                Collections.reverse(locales);

                AcceptedLanguageCodeType mainLocale = AcceptedLanguageCodeType.valueOfLanguage(
                        locales.get(FIRST_ELEMENT_INDEX).getRange()
                );

                AuthUserService.getInstance().setUserLocale(mainLocale);
            } else {
                AuthUserService.getInstance().setUserLocale(LanguageCodeType.en);
            }
        } catch (Exception exception) {
            AuthUserService.getInstance().setUserLocale(LanguageCodeType.en);
        }

        return context.proceed();
    }
}
