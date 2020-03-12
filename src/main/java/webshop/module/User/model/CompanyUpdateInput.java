package webshop.module.User.model;

import webshop.core.service.ExceptionService;
import webshop.module.User.service.PasswordEncryptService;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class CompanyUpdateInput {
    @HeaderParam("Content-Type")
    private String contentType;

    @NotNull
    private long id;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    @FormParam("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    @FormParam("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    @FormParam("password")
    public void setPassword(String password) {
        try {
            this.password = PasswordEncryptService.generateStrongPasswordHash(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            ExceptionService.throwIlIllegalArgumentException(
                    this.getClass(),
                    "Company Create Params: Password could not be encrypted!",
                    "Company Create Params: Password encryption algorithm not found!",
                    Response.Status.BAD_REQUEST
            );
        }
    }


    public Company toCompany() {
        Company company = new Company();
        company.setId(getId());
        company.setEmail(getEmail());
        company.setName(getName());
        company.setPassword(getPassword());

        return company;
    }
}
