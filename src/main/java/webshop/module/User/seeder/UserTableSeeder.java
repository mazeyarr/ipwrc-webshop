package webshop.module.User.seeder;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import webshop.core.iinterface.CoreSeeder;
import webshop.core.iinterface.CoreValue;
import webshop.core.iinterface.Seeder;
import webshop.module.User.model.User;
import webshop.module.User.service.PasswordEncryptService;
import webshop.module.User.service.UserService;
import webshop.module.User.type.UserType;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserTableSeeder extends CoreSeeder implements Seeder {
    private final String SEEDER_NAME = "UserTableSeeder";
    public static final int AMOUNT_OF_SEEDING_USERS = 1000;

    @Override
    public boolean isAlreadySeeded() {
        try {
            User user = UserService.findOrFailUserById(CoreValue.FIRST_ID);

            return user.getEmail().equals("mazeyarr@gmail.com");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void up() {
        Random r = new Random();
        User me = new User();

        me.setFirstName("Mazeyar");
        me.setLastName("Rezaei");
        me.setEmail("mazeyarr@gmail.com");
        me.setCompany(UserService.findCompanyById(CoreValue.FIRST_ID));

        UserService.createUser(me, UserType.SUPERADMIN);

        try {
            me.setPassword(PasswordEncryptService.generateStrongPasswordHash("12345"));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("nl"), new RandomService()
        );
        Faker faker = new Faker(new Locale("nl"));

        for (int i = 0; i < AMOUNT_OF_SEEDING_USERS; i++) {
            try {
                String email = fakeValuesService.bothify("???????#####@gmail.com");
                Matcher emailMatcher = Pattern.compile("\\w{4}\\d{2}@gmail.com").matcher(email);

                if (!emailMatcher.find()) {
                    throw new Exception("Email that was generated was invalid " + email);
                }

                User user = new User();

                user.setEmail(email);
                user.setPassword(PasswordEncryptService.generateStrongPasswordHash("12345"));
                user.setFirstName(faker.name().firstName());
                user.setLastName(faker.name().lastName());

                if (r.nextBoolean()) {
                    user.setCompany(UserService.findCompanyById(new Random()
                            .nextInt(
                                    CompanyTableSeeder.AMOUNT_OF_SEEDING_COMPANIES - 1
                            ) + 1
                    ));
                }

                UserService.createUser(user, UserType.USER);

                log(user.getId());
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
