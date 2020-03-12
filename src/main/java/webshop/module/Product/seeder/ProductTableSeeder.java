package webshop.module.Product.seeder;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import webshop.core.iinterface.CoreSeeder;
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

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductTableSeeder extends CoreSeeder implements Seeder {
    private final String SEEDER_NAME = "ProductTableSeeder";

    @Override
    public boolean isAlreadySeeded() {
        try {
            Product user = ProductService.findOrFailProductById(1);

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
        myProduct.setDueDate(new Date());

        myProduct.setManufacturer(UserService.findCompanyById(1));
        myProduct.setCreatedBy(UserService.findUserById(1));

        return myProduct;
    }

    @Override
    public void up() {
        Random r = new Random();

        Product defaultProduct = getDefaultProduct();

        ProductService.createProduct(defaultProduct);

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("nl"),
                new RandomService()
        );
        Faker faker = new Faker(new Locale("nl"));

        for (int i = 0; i < 1000; i++) {
            try {
                Product product = new Product();

                product.setName(faker.commerce().productName());
                product.setDescription(faker.commerce().promotionCode());
                product.setProductType(faker.commerce().material());
                product.setPrice(
                        (float) Double.parseDouble(
                                faker.commerce().price(0, 100).replace(',', '.')
                        )
                );
                product.setDueDate(new Date());

                if (r.nextBoolean()) {
                    Company company = UserService.findCompanyById(new Random()
                            .nextInt(
                                    CompanyTableSeeder.AMOUNT_OF_SEEDING_COMPANIES - 1
                            ) + 1
                    );
                    User createdBy;

                    if (company.getEmployees().size() < 1) {
                        User[] users = (User[]) company.getEmployees().toArray();
                        createdBy = users[0];
                    } else {
                        createdBy = UserService.findUserById(1);
                    }

                    product.setManufacturer(company);
                    product.setCreatedBy(createdBy);
                }

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
