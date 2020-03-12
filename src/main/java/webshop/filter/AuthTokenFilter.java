package webshop.filter;

import webshop.core.iinterface.Translator;
import webshop.core.providers.TokenProvider;
import webshop.core.service.ExceptionService;
import webshop.filter.bindings.AuthBinding;
import webshop.module.User.service.AuthUserService;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Authentication Filter that prevents users to use resources that they are not permitted for
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
@Provider
@AuthBinding
@Priority(Priorities.AUTHENTICATION)
public class AuthTokenFilter implements ContainerRequestFilter {
    ContainerRequestContext mContext;
    TokenProvider tokenProvider;

    String token;

    @Override
    public void filter(ContainerRequestContext context) {
        this.mContext = context;
        tokenProvider = TokenProvider.getInstance();
        token = "";

        proceedIfHasAuthorizationHeader();
        setAuthorizationTokenFromHeader();
        proceedIfTokenIsNotEmpty(getToken());
        proceedIfTokenIsValid(getToken());

        AuthUserService.getInstance().setAuthUserId(
                tokenProvider.getDecodedJWT(getToken())
                        .getClaim(TokenProvider.CLAIM_USER_ID_KEY).asLong()
        );
    }

    private void proceedIfHasAuthorizationHeader() {
        if (!getContext().getHeaders().containsKey("Authorization")) {
            ExceptionService.throwIlIllegalArgumentException(
                    this.getClass(),
                    Translator.translate("Authorization Failed: Token not provided"),
                    Translator.translate("Authorization key not provided"),
                    Response.Status.BAD_REQUEST
            );
        }
    }

    private void proceedIfTokenIsNotEmpty(String token) {
        if (token.equals("")) {
            ExceptionService.throwIlIllegalArgumentException(
                    this.getClass(),
                    Translator.translate("Authorization Failed: Token was empty"),
                    Translator.translate("Token was empty in the Authorization header key"),
                    Response.Status.BAD_REQUEST
            );
        }
    }

    private void proceedIfTokenIsValid(String token) {
        if (!getTokenProvider().verifyToken(token)) {
            ExceptionService.throwIlIllegalArgumentException(
                    this.getClass(),
                    Translator.translate("Invalid token!"),
                    Translator.translate("Token verification failed!"),
                    Response.Status.UNAUTHORIZED
            );
        }
    }

    private ContainerRequestContext getContext() {
        return mContext;
    }

    private TokenProvider getTokenProvider() {
        return tokenProvider;
    }

    private String getToken() {
        return token;
    }

    private void setAuthorizationTokenFromHeader() {
        this.token = getContext().getHeaders().getFirst("Authorization");
    }
}
