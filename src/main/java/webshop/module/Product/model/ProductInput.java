package webshop.module.Product.model;

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

public class ProductInput {
    @HeaderParam("Content-Type")
    private String contentType;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String productType;

    @NotNull
    private Float price;

    private LocalDate dueDate;

    @NotNull
    private int discount;

    @NotNull
    private String discountDescription;

    @NotNull
    private long manufacturerId;

    public String getContentType() {
        return contentType;
    }

    @HeaderParam("Content-Type")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    @FormParam("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @FormParam("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductType() {
        return productType;
    }

    @FormParam("productType")
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public @NotNull Float getPrice() {
        return price;
    }

    @FormParam("price")
    public void setPrice(@NotNull Float price) {
        this.price = price;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @FormParam("dueDate")
    public void setDueDate(String dueDate) {
        this.dueDate = LocalDate.parse(dueDate);
    }

    public Set<ProductDiscount> getDiscount() {
        Set<ProductDiscount> discounts = new HashSet<>();
        ProductDiscount productDiscount = new ProductDiscount();

        productDiscount.setDiscount(discount);
        productDiscount.setDescription(getDiscountDescription());
        productDiscount.setType(DiscountType.PERCENTAGE);

        if (discount != 0) {
            discounts.add(productDiscount);
        }


        return discounts;
    }

    @FormParam("discount")
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

    @FormParam("discountDescription")
    public void setDiscountDescription(String description) {
        this.discountDescription = description;
    }

    public Company getManufacturer() throws UserNotFoundException {
        return UserService.findOrFailCompanyById(getManufacturerId());
    }

    public long getManufacturerId() {
        return manufacturerId;
    }

    @FormParam("manufacturerId")
    public void setManufacturerId(long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public User getCreatedBy() {
        return AuthUserService.getInstance().getAuthUser();
    }

    public Product toProduct() {
        Product product = new Product();

        product.setName(getName());
        product.setDescription(getDescription());
        product.setProductType(getProductType());
        product.setPrice(getPrice());
        product.setDueDate(getDueDate());
        product.setProductDiscounts(getDiscount());
        product.setManufacturer(AuthUserService.getInstance().getAuthUser().getCompany());
        product.setCreatedBy(AuthUserService.getInstance().getAuthUser());

        return product;
    }
}
