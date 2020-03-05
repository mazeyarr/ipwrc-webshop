package webshop.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

public class ExceptionService {
  public static void throwIlIllegalArgumentException(Class source, String message, String cause, Response.Status status) {
    throw new WebApplicationException(
        message,
        new IllegalArgumentException("--- "+ source.getName() + " " + cause + " ---"),
        status);
  }

  public static Response toResponse(Exception exception, int HttpStatusCode) {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode errorNode = mapper.createObjectNode();

    errorNode.put("error", true);
    errorNode.put("message", exception.getMessage());
    errorNode.put("code", HttpStatusCode);
    errorNode.put("time", new Date().toString());

    return Response.status(HttpStatusCode)
            .entity(errorNode)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }
}
