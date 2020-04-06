package webshop.module.Product.resource;

import com.amazonaws.services.xray.model.Http;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.hibernate.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;
import webshop.core.iinterface.CoreValue;
import webshop.core.iinterface.Translator;
import webshop.core.service.CoreHelper;
import webshop.core.service.ExceptionService;
import webshop.filter.bindings.AuthBinding;
import webshop.module.Product.exception.ProductNotFoundException;
import webshop.module.Product.model.Product;
import webshop.module.Product.model.ProductInput;
import webshop.module.Product.model.ProductUpdateInput;
import webshop.module.Product.seeder.ProductTableSeeder;
import webshop.module.Product.service.ProductService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    @GET
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response getProductById(@PathParam("id") Long id) {
        try {
            return Response.status(HttpStatus.OK_200)
                    .entity(ProductService.findOrFailProductById(id))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (ProductNotFoundException e) {
            return ExceptionService.toResponse(e, HttpStatus.BAD_REQUEST_400);
        }
    }

    @GET
    @Path("/")
    @UnitOfWork
    @AuthBinding
    public List<Product> getProductAll() {
        return ProductService.getAllProducts();
    }

    @POST
    @Path("/")
    @AuthBinding
    @UnitOfWork
    public Response createProduct(@BeanParam ProductInput productInput) {
        Product product = ProductService.createProduct(productInput.toProduct());

        return Response.status(HttpStatus.OK_200)
                .entity(product)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response updateProduct(@PathParam("id") long id,
                               @BeanParam ProductUpdateInput productUpdateInput) {
        try {
            Product updatedProduct = ProductService.updateProduct(productUpdateInput.toProduct());

            return Response.status(HttpStatus.OK_200)
                    .entity(updatedProduct)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (ProductNotFoundException productNotFoundException) {
            return ExceptionService.toResponse(productNotFoundException, HttpStatus.BAD_REQUEST_400);
        }
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            if (ProductService.deleteProductById(id)) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode responseNode = mapper.createObjectNode();

                responseNode.put("error", false);
                responseNode.put("message", Translator.translate(
                        "Product has been deleted"
                ));
                responseNode.put("time", new Date().toString());

                return CoreHelper.toResponseWithNode(responseNode, HttpStatus.OK_200);
            }

            return ExceptionService.toResponse(
                    new Exception("Could not delete product!"),
                    HttpStatus.BAD_REQUEST_400
            );
        } catch (ProductNotFoundException productNotFoundException) {
            return ExceptionService.toResponse(productNotFoundException, HttpStatus.BAD_REQUEST_400);
        }
    }

    @GET
    @Path("/seed")
    @UnitOfWork
    public Response seed() {
        new ProductTableSeeder().run(CoreValue.ON);

        return Response.status(HttpStatus.OK_200)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
