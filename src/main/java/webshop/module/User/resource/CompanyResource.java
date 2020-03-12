package webshop.module.User.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;
import webshop.core.iinterface.Translator;
import webshop.core.service.ExceptionService;
import webshop.filter.bindings.AuthBinding;
import webshop.module.User.exception.UserNotFoundException;
import webshop.module.User.model.*;
import webshop.module.User.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
public class CompanyResource {
    @GET
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response getCompanyById(@PathParam("id") Long id) {
        try {
            return Response.status(HttpStatus.OK_200)
                    .entity(UserService.findOrFailCompanyById(id))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (UserNotFoundException e) {
            return ExceptionService.toResponse(e, HttpStatus.BAD_REQUEST_400);
        }
    }

    @GET
    @Path("/")
    @UnitOfWork
    @AuthBinding
    public List<Company> getCompanyAll() {
        return UserService.getAllCompanies();
    }

    @POST
    @Path("/")
    @UnitOfWork
    public Company createCompany(@BeanParam CompanyInput companyInput) {
        return UserService.createCompany(companyInput.toCompany());
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @AuthBinding
    public Response updateCompany(@PathParam("id") long id,
                               @BeanParam CompanyUpdateInput companyUpdateInput) {
        try {
            Company updateCompany = UserService.updateCompany(companyUpdateInput.toCompany());

            return Response.status(HttpStatus.OK_200)
                    .entity(updateCompany)
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
    public Response deleteCompany(@PathParam("id") Long id) {
        try {
            if (UserService.deleteCompanyById(id)) {
                return Response.status(HttpStatus.OK_200)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return ExceptionService.toResponse(
                    new Exception(Translator.translate(
                            "Could not delete company!"
                    )),
                    HttpStatus.BAD_REQUEST_400
            );
        } catch (UserNotFoundException userNotFoundException) {
            return ExceptionService.toResponse(userNotFoundException, HttpStatus.BAD_REQUEST_400);
        }
    }
}
