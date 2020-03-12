package webshop.module.User.seeder;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import webshop.core.iinterface.CoreSeeder;
import webshop.core.iinterface.Seeder;
import webshop.module.User.model.Company;
import webshop.module.User.model.User;
import webshop.module.User.service.PasswordEncryptService;
import webshop.module.User.service.UserService;
import webshop.module.User.type.UserType;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompanyTableSeeder extends CoreSeeder implements Seeder {
    private final String SEEDER_NAME = "CompanyTableSeeder";
    public static final int AMOUNT_OF_SEEDING_COMPANIES = 100;

    @Override
    public boolean isAlreadySeeded() {
        try {
            Company user = UserService.findCompanyById(1);

            return user.getEmail().equals("test@test.nl");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void up() {
        Company testComp = new Company();

        testComp.setEmail("test@test.nl");
        testComp.setName("Test Company");

        try {
            testComp.setPassword(PasswordEncryptService.generateStrongPasswordHash("12345"));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        UserService.createCompany(testComp);

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("nl"), new RandomService()
        );
        Faker faker = new Faker(new Locale("nl"));

        for (int i = 0; i < AMOUNT_OF_SEEDING_COMPANIES; i++) {
            try {
                String name = faker.company().name();
                String email = fakeValuesService.bothify("???????#####@gmail.com");
                Matcher emailMatcher = Pattern.compile("\\w{4}\\d{2}@gmail.com").matcher(email);

                if (!emailMatcher.find()) {
                    throw new Exception("Email that was generated was invalid: " + email);
                }

                Company company = new Company();

                company.setEmail(email);
                company.setName(name);
                company.setPassword(PasswordEncryptService.generateStrongPasswordHash("12345"));

                UserService.createCompany(company);

                log(company.getId());
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
