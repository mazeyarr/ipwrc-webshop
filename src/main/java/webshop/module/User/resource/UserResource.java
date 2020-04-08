package webshop.module.User.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;
import webshop.core.iinterface.CoreValue;
import webshop.core.service.ExceptionService;
import webshop.filter.bindings.AuthBinding;
import webshop.module.User.exception.UserNotFoundException;
import webshop.module.User.model.User;
import webshop.module.User.model.UserInput;
import webshop.module.User.model.UserUpdateInput;
import webshop.module.User.seeder.CompanyTableSeeder;
import webshop.module.User.seeder.UserTableSeeder;
import webshop.module.User.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @GET
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response getUserById(@PathParam("id") Long id) {
        try {
            return Response.status(HttpStatus.OK_200)
                    .entity(UserService.findOrFailUserById(id))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (UserNotFoundException e) {
            return ExceptionService.toResponse(e, HttpStatus.BAD_REQUEST_400);
        }
    }

    @GET
    @UnitOfWork
    @AuthBinding
    public List<User> getUserAll() {
        return UserService.getAllUsers();
    }

    @POST
    @UnitOfWork
    public User createUser(@BeanParam UserInput userInput) {
        return UserService.createUser(userInput.toUser(), userInput.getRole());
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response updateUser(@PathParam("id") long id,
                               @BeanParam UserUpdateInput userUpdateInput) {
        try {
            User updatedUser = UserService.updateUser(userUpdateInput.toUser());

            return Response.status(HttpStatus.OK_200)
                    .entity(updatedUser)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (UserNotFoundException userNotFoundException) {
            return ExceptionService.toResponse(userNotFoundException, HttpStatus.BAD_REQUEST_400);
        }
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            if (UserService.deleteUserById(id)) {
                return Response.status(HttpStatus.OK_200)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return ExceptionService.toResponse(
                    new Exception("Could not delete user!"),
                    HttpStatus.BAD_REQUEST_400
            );
        } catch (UserNotFoundException userNotFoundException) {
            return ExceptionService.toResponse(userNotFoundException, HttpStatus.BAD_REQUEST_400);
        }
    }

    @GET
    @Path("/seed")
    @UnitOfWork
    public Response seed() {
        new CompanyTableSeeder().run(CoreValue.OFF);
        new UserTableSeeder().run(CoreValue.OFF);

        return Response.status(HttpStatus.OK_200)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
