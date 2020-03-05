package webshop.module.User.model;

import webshop.core.service.ExceptionService;
import webshop.core.service.LookupService;
import webshop.module.User.service.PasswordEncryptService;
import webshop.module.User.type.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AuthInput {
    @HeaderParam("Content-Type")
    private String contentType;

    @Email
    @NotNull
    private String username;

    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    @FormParam("username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @FormParam("password")
    public void setPassword(String password) {
        this.password = password;
    }
}
