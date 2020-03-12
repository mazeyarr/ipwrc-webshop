package webshop.module.Product.model;

import webshop.core.iinterface.MyEntity;
import webshop.module.User.model.Company;
import webshop.module.User.model.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "price")
    private Float price;

    @Column(name = "due_date")
    private Date dueDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private Set<ProductDiscount> productDiscounts;

    @ManyToOne
    @JoinColumn(name = "manufacturer_user_id")
    private Company manufacturer;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    public Product() {
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Set<ProductDiscount> getProductDiscounts() {
        return productDiscounts;
    }

    public void setProductDiscounts(Set<ProductDiscount> productDiscounts) {
        this.productDiscounts = productDiscounts;
    }

    public Company getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Company manufacturer) {
        this.manufacturer = manufacturer;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
