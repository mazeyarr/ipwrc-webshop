package webshop.module.Product.model;

import webshop.module.User.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;

public class ProductUpdateInput {
    @HeaderParam("Content-Type")
    private String contentType;

    @NotNull
    private long id;
}
