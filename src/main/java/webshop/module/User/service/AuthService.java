package webshop.module.User.service;

import webshop.core.providers.TokenProvider;
import webshop.core.service.TranslateService;
import webshop.module.User.exception.InvalidTokenException;
import webshop.module.User.exception.UserNotFoundException;
import webshop.module.User.exception.ValidationException;
import webshop.module.User.model.User;
import webshop.core.iinterface.Translator;

import javax.persistence.NoResultException;

public class AuthService {
    public static User loginUser(
            String username,
            String password
    ) throws NoResultException, ValidationException, UserNotFoundException {
        TokenProvider tokenProvider = TokenProvider.getInstance();

        User authUser = UserService.findOrFailUserByUsername(username);

        validateOrFailUserByPassword(password, authUser.getPassword());

        authUser.setToken(tokenProvider.generateToken(authUser.getId()));

        return authUser;
    }

    public static User loginWithToken(String token) throws InvalidTokenException, UserNotFoundException {
        TokenProvider tokenProvider = TokenProvider.getInstance();

        if (!tokenProvider.verifyToken(token)) {
            throw new InvalidTokenException(Translator.translate("Token was invalid!"));
        }

        User authUser = UserService.findOrFailUserById(
                tokenProvider.getDecodedJWT(token)
                        .getClaim(TokenProvider.CLAIM_USER_ID_KEY)
                        .asLong()
        );

        authUser.setToken(token);

        AuthUserService.getInstance().setAuthUser(authUser);

        return authUser;
    }

    private static void validateOrFailUserByPassword(String givenPassword,
                                                     String password) throws ValidationException {
        if (!PasswordDecryptService.validatePassword(givenPassword, password)) {
            throw new ValidationException(Translator.translate(
                    "Username and Password combination is invalid!"
            ));
        }
    }
}
