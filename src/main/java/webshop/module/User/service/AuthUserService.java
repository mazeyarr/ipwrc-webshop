package webshop.module.User.service;

import webshop.module.User.model.User;
import webshop.type.AcceptedLanguageCodeType;
import webshop.type.LanguageCodeType;

public class AuthUserService {
    private static volatile AuthUserService AUTH_SERVICE_INSTANCE;

    private long authUserId;

    private User authUser;

    private LanguageCodeType userLocale;

    public AuthUserService() {
    }

    public AuthUserService(long authUserId) {
        this.authUserId = authUserId;
        this.authUser = UserService.findUserById(authUserId);
    }

    public long getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(long authUserId) {
        this.authUserId = authUserId;
    }

    public User getAuthUser() {
        return authUser;
    }

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }

    public LanguageCodeType getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(LanguageCodeType userLocale) {
        this.userLocale = userLocale;
    }

    public void setUserLocale(AcceptedLanguageCodeType userLocale) {
        this.userLocale = LanguageCodeType.valueOf(userLocale.acceptedLanguage);
    }

    private void setUserIfNotSetAndCanBeSet() {
        if (AUTH_SERVICE_INSTANCE.authUserId != 0
                &&
                AUTH_SERVICE_INSTANCE.authUser == null
        ) {
            AUTH_SERVICE_INSTANCE.authUser = UserService.findUserById(
                    AUTH_SERVICE_INSTANCE.authUserId
            );
        }
    }

    public static AuthUserService getInstance() {
        if (AUTH_SERVICE_INSTANCE == null) {
            synchronized (AuthUserService.class) {
                if (AUTH_SERVICE_INSTANCE == null) {
                    AUTH_SERVICE_INSTANCE = new AuthUserService();
                }
            }
        } else{
            AUTH_SERVICE_INSTANCE.setUserIfNotSetAndCanBeSet();
        }

        return AUTH_SERVICE_INSTANCE;
    }
}
