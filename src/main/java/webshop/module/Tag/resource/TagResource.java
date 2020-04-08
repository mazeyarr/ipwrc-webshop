package webshop.module.Tag.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.hibernate.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;
import webshop.core.iinterface.CoreValue;
import webshop.core.iinterface.Translator;
import webshop.core.service.CoreHelper;
import webshop.core.service.ExceptionService;
import webshop.filter.bindings.AuthBinding;
import webshop.module.Tag.exception.TagNotFoundException;
import webshop.module.Tag.model.Tag;
import webshop.module.Tag.model.TagInput;
import webshop.module.Tag.model.TagUpdateInput;
import webshop.module.Tag.service.TagService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("tags")
@Produces(MediaType.APPLICATION_JSON)
public class TagResource {
    @GET
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response getTagById(@PathParam("id") Long id) {
        try {
            return Response.status(HttpStatus.OK_200)
                    .entity(TagService.findOrFailTagById(id))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (TagNotFoundException e) {
            return ExceptionService.toResponse(e, HttpStatus.BAD_REQUEST_400);
        }
    }

    @GET
    @UnitOfWork
    @AuthBinding
    public List<Tag> getTagAll() {
        return TagService.getAllTags();
    }

    @POST
    @AuthBinding
    @UnitOfWork
    public Response createTag(@BeanParam TagInput tagInput) {
        Tag tag = TagService.createTag(tagInput.toTag());

        return Response.status(HttpStatus.OK_200)
                .entity(tag)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response updateTag(@BeanParam TagUpdateInput tagUpdateInput) {
        Tag updatedTag = TagService.updateTag(tagUpdateInput.toTag());

        return Response.status(HttpStatus.OK_200)
                .entity(updatedTag)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response deleteTag(@PathParam("id") Long id) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode responseNode = mapper.createObjectNode();

            if (TagService.deleteTagById(id)) {
                responseNode.put("error", false);
                responseNode.put("message", Translator.translate(
                        "Tag has been deleted"
                ));
                responseNode.put("time", new Date().toString());

                return CoreHelper.toResponseWithNode(responseNode, HttpStatus.OK_200);
            }

            return ExceptionService.toResponse(
                    new Exception(Translator.translate("Could not delete tag!")),
                    HttpStatus.BAD_REQUEST_400
            );
        } catch (TagNotFoundException tagNotFoundException) {
            return ExceptionService.toResponse(tagNotFoundException, HttpStatus.BAD_REQUEST_400);
        }
    }
}
