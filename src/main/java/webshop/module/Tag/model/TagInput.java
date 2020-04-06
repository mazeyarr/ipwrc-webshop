package webshop.module.Tag.model;

import webshop.core.service.CoreHelper;
import webshop.module.Product.model.Product;
import webshop.module.Product.model.ProductDiscount;
import webshop.module.Product.type.DiscountType;
import webshop.module.User.exception.UserNotFoundException;
import webshop.module.User.model.Company;
import webshop.module.User.model.User;
import webshop.module.User.service.AuthUserService;
import webshop.module.User.service.UserService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class TagInput {
    @HeaderParam("Content-Type")
    private String contentType;

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    @FormParam("name")
    public void setName(String name) {
        this.name = name;
    }

    public Tag toTag() {
        Tag tag = new Tag();

        tag.setName(getName());

        return tag;
    }
}
