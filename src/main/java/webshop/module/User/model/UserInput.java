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

public class UserInput {
    @HeaderParam("Content-Type")
    private String contentType;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String password;

    private UserType role;

    public String getEmail() {
        return email;
    }

    @FormParam("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    @FormParam("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @FormParam("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
                    "User Create Params: Password could not be encrypted!",
                    "User Create Params: Password encryption algorithm not found!",
                    Response.Status.BAD_REQUEST
            );
        }
    }

    public UserType getRole() {
        return role;
    }

    @FormParam("role")
    public void setRole(String role) {
        if (LookupService.isStringEnumValue(UserType.class, role)) {
            this.role = UserType.valueOf(role);
            return;
        }

        this.role = UserType.USER;
    }

    public User toUser() {
        return new User(
                email,
                firstName,
                lastName,
                password
        );
    }
}
