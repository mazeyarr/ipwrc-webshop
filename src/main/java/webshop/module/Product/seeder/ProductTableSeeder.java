package webshop.module.Product.seeder;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import webshop.core.iinterface.CoreSeeder;
import webshop.core.iinterface.CoreValue;
import webshop.core.iinterface.Seeder;
import webshop.module.Product.model.Product;
import webshop.module.Product.model.ProductDiscount;
import webshop.module.Product.service.ProductService;
import webshop.module.Product.type.DiscountType;
import webshop.module.User.model.Company;
import webshop.module.User.model.User;
import webshop.module.User.seeder.CompanyTableSeeder;
import webshop.module.User.service.PasswordEncryptService;
import webshop.module.User.service.UserService;
import webshop.module.User.type.UserType;
import webshop.type.LanguageCodeType;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductTableSeeder extends CoreSeeder implements Seeder {
    private final String SEEDER_NAME = "ProductTableSeeder";
    public static final int AMOUNT_OF_SEEDING_PRODUCTS = 1000;

    @Override
    public boolean isAlreadySeeded() {
        try {
            Product user = ProductService.findOrFailProductById(CoreValue.FIRST_ID);

            return user.getName().equals("Default Product");
        } catch (Exception e) {
            return false;
        }
    }

    private Product getDefaultProduct() {
        Product myProduct = new Product();
        myProduct.setName("Default Product");
        myProduct.setDescription("Default Description");
        myProduct.setProductType("Drink");
        myProduct.setPrice(2.00f);
        myProduct.setDueDate(LocalDate.parse("2019-09-01"));

        myProduct.setManufacturer(UserService.findCompanyById(CoreValue.FIRST_ID));
        myProduct.setCreatedBy(UserService.findUserById(CoreValue.FIRST_ID));

        return myProduct;
    }

    @Override
    public void up() {
        Random r = new Random();
        Faker faker = new Faker(new Locale(LanguageCodeType.nl.toString()));
        Product defaultProduct = getDefaultProduct();

        ProductService.createProduct(defaultProduct);

        for (int i = 0; i < AMOUNT_OF_SEEDING_PRODUCTS; i++) {
            try {
                Product product = new Product();
                Company company = UserService.findCompanyById(r.nextInt(
                        CompanyTableSeeder.AMOUNT_OF_SEEDING_COMPANIES - 1
                        )
                );
                User createdBy;

                if (company.getEmployees().size() > CoreValue.EMPTY) {
                    Object[] objects = company.getEmployees().toArray();
                    createdBy = (User) objects[CoreValue.FIRST_INDEX];
                } else {
                    createdBy = UserService.findUserById(CoreValue.FIRST_ID);
                }

                product.setName(faker.commerce().productName());
                product.setDescription(faker.commerce().promotionCode());
                product.setProductType(faker.commerce().material());
                product.setPrice(
                        (float) Double.parseDouble(
                                faker.commerce().price(0, 100).replace(',', '.')
                        )
                );
                product.setDueDate(LocalDate.now());

                product.setManufacturer(company);
                product.setCreatedBy(createdBy);

                ProductService.createProduct(product);

                if (r.nextInt(3) == 1) {
                    ProductDiscount discount = new ProductDiscount();

                    discount.setProduct(product);
                    discount.setDiscount(faker.number().numberBetween(0, 100));
                    discount.setDescription(faker.commerce().promotionCode());

                    if (r.nextInt(3) == 1) {
                        discount.setType(DiscountType.PERCENTAGE);
                    } else {
                        discount.setType(DiscountType.VALUE);
                    }

                    ProductService.createProductDiscount(discount);
                }

                log(product.getId());
            } catch (Exception e) {
                e.printStackTrace();
                i--;
            }
        }
    }

    @Override
    public void down() {

    }

    @Override
    public void log(long id) {
        System.out.println(SEEDER_NAME + " Seeding id: " + id);
    }
}
