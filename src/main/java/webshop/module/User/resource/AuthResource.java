package webshop.module.User.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;
import webshop.core.service.ExceptionService;
import webshop.module.User.exception.InvalidTokenException;
import webshop.module.User.exception.UserNotFoundException;
import webshop.module.User.exception.ValidationException;
import webshop.module.User.model.AuthInput;
import webshop.module.User.model.User;
import webshop.module.User.service.AuthService;

import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @POST
    @Path("/login/token")
    @UnitOfWork
    public Response loginWithToken(@HeaderParam("Authorization") String token) {
        try {
            User authUser = AuthService.loginWithToken(token);

            return Response
                    .status(HttpStatus.OK_200)
                    .entity(authUser)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoResultException | InvalidTokenException | UserNotFoundException exception) {
            return ExceptionService.toResponse(
                    exception,
                    HttpStatus.UNAUTHORIZED_401
            );
        }
    }

    @POST
    @Path("/login")
    @UnitOfWork
    public Response loginUser(@BeanParam AuthInput authInput) {
        try {
            User authUser = AuthService.loginUser(
                    authInput.getUsername(),
                    authInput.getPassword()
            );

            return Response
                    .status(HttpStatus.OK_200)
                    .entity(authUser)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (ValidationException | NoResultException | UserNotFoundException exception) {
            return ExceptionService.toResponse(
                    exception,
                    HttpStatus.UNAUTHORIZED_401
            );
        }
    }
}
