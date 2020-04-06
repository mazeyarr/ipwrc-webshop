package webshop.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

public class CoreHelper {
    private final static int ZERO = 0;

    public static boolean isNotZero(int value) {
        return value != ZERO;
    }

    public static Response toEmptyResponse(int HttpStatusCode) {
        return Response.status(HttpStatusCode)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public static Response toResponseWithNode(ObjectNode node, int HttpStatusCode) {
        return Response.status(HttpStatusCode)
                .entity(node)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
