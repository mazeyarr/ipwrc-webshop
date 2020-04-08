package webshop.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.hibernate.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;
import webshop.core.iinterface.CoreValue;
import webshop.core.iinterface.Translator;
import webshop.core.service.ExceptionService;
import webshop.module.Product.seeder.ProductTableSeeder;
import webshop.module.User.exception.InvalidTokenException;
import webshop.module.User.exception.UserNotFoundException;
import webshop.module.User.exception.ValidationException;
import webshop.module.User.model.AuthInput;
import webshop.module.User.model.User;
import webshop.module.User.seeder.CompanyTableSeeder;
import webshop.module.User.seeder.UserTableSeeder;
import webshop.module.User.service.AuthService;

import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class WebshopResource {
    @GET
    @UnitOfWork
    public Response version() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode versionNode = mapper.createObjectNode();

        versionNode.put("version", "Docker v1.1.1");

        return Response
                .status(HttpStatus.OK_200)
                .entity(versionNode)
                .type(MediaType.APPLICATION_JSON)
                .build();

    }

    @POST
    @Path("/seed")
    @UnitOfWork
    public Response seedDatabase() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode seedingNode = mapper.createObjectNode();;

        try {
            new CompanyTableSeeder().run(CoreValue.OFF);
            new UserTableSeeder().run(CoreValue.OFF);
            new ProductTableSeeder().run(CoreValue.ON);

            seedingNode.put("error", false);
            seedingNode.put("message", Translator.translate(
                    "Database has been seeded!"
            ));

            return Response
                    .status(HttpStatus.OK_200)
                    .entity(seedingNode)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            seedingNode.put("error", true);
            seedingNode.put("message", Translator.translate(
                    "Database has not been seeded, an error occured!"
            ));
            seedingNode.put("errorMessage", e.getMessage());

            return Response
                    .status(HttpStatus.OK_200)
                    .entity(seedingNode)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
