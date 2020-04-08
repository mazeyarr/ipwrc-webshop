package webshop.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
}
