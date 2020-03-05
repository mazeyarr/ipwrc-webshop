package webshop.module.User.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import webshop.core.iinterface.MyEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "e_mail", unique = true)
    @JsonProperty
    private String email;

    @Column(name = "first_name")
    @JsonProperty
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty
    private String lastName;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Transient
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRole> roles = new HashSet<>();

    public User() {
    }

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> userRoles) {
        this.roles = userRoles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
