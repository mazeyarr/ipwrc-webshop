package webshop.module.Product.model;

import webshop.core.iinterface.MyEntity;
import webshop.module.User.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product_discounts")
public class ProductDiscount implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "description")
    private String description;

    public ProductDiscount() {
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
