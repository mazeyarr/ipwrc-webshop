package webshop.module.Tag.model;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;

public class TagUpdateInput {
    @HeaderParam("Content-Type")
    private String contentType;

    @NotNull
    private long id;

    @NotNull
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @FormParam("name")
    public void setName(String name) {
        this.name = name;
    }

    public Tag toTag() {
        Tag tag = new Tag();

        tag.setId(getId());
        tag.setName(getName());

        return tag;
    }
}
