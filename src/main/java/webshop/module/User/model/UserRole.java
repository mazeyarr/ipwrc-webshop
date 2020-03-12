package webshop.module.User.model;

import com.fasterxml.jackson.annotation.*;
import webshop.core.iinterface.MyEntity;
import webshop.module.User.type.UserType;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserRole implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @JsonProperty
    @Column(name = "role")
    private String role;

    @JsonIgnore
    @Column(name = "user_id")
    private long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public UserRole() {
    }

    public UserRole(String role, User user) {
        this.role = role;
        this.userId = user.getId();
        this.user = user;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public UserType getRole() {
        return UserType.valueOf(role);
    }

    public void setRole(UserType role) {
        this.role = role.toString();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
