package webshop.interceptor;

import webshop.module.User.service.AuthUserService;
import webshop.type.LanguageCodeType;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocaleInterceptor implements ReaderInterceptor {
    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        MultivaluedMap<String, String> headers = context.getHeaders();
//        List<Locale.LanguageRange> locales = Locale.LanguageRange.parse(acceptLanguage);

//        if (locales.size() > 1) {
//            System.out.println("here");
//        } else {
//            AuthUserService.getInstance().setUserLocale(LanguageCodeType.nl);
//        }

        return context.proceed();
    }
}
