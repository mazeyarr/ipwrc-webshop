package webshop.module.User.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;

public class UserUpdateInput {
    @HeaderParam("Content-Type")
    private String contentType;

    @NotNull
    private long id;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String password;

    public long getId() {
        return id;
    }
    @FormParam("id")
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

    public User toUser() {
        User user = new User(
                email,
                firstName,
                lastName,
                password
        );

        user.setId(this.getId());

        return user;
    }
}
