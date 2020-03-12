package webshop.module.User;

import io.dropwizard.setup.Environment;
import webshop.core.iinterface.CoreModule;
import webshop.core.service.CoreService;
import webshop.filter.AuthTokenFilter;
import webshop.interceptor.LocaleInterceptor;
import webshop.module.User.dao.CompanyDao;
import webshop.module.User.dao.UserDao;
import webshop.module.User.dao.UserRoleDao;
import webshop.module.User.model.Company;
import webshop.module.User.model.User;
import webshop.module.User.model.UserRole;
import webshop.module.User.resource.AuthResource;
import webshop.module.User.resource.CompanyResource;
import webshop.module.User.resource.UserResource;
import webshop.module.User.seeder.CompanyTableSeeder;
import webshop.module.User.seeder.UserTableSeeder;

public class UserModule extends CoreModule<UserDao> {
    private static volatile UserModule USER_MODULE_INSTANCE;
    private UserDao userDao;
    private CompanyDao companyDao;
    private UserRoleDao userRoleDao;

    public UserModule() {
        addEntityToModule(User.class);
        addEntityToModule(Company.class);
        addEntityToModule(UserRole.class);
    }

    @Override
    public void init(Environment environment) {
        initDao();
        initResources(environment);
    }

    @Override
    public void initResources(Environment environment) {
        environment.jersey().register(new AuthTokenFilter());
        environment.jersey().register(new LocaleInterceptor());

        environment.jersey().register(new UserResource());
        environment.jersey().register(new CompanyResource());
        environment.jersey().register(new AuthResource());
    }

    @Override
    public void initDao() {
        userDao = new UserDao(
                CoreService.getInstance()
                        .getHibernate()
                        .getSessionFactory()
        );

        companyDao = new CompanyDao(
                CoreService.getInstance()
                        .getHibernate()
                        .getSessionFactory()
        );

        userRoleDao = new UserRoleDao(
                CoreService.getInstance()
                        .getHibernate()
                        .getSessionFactory()
        );
    }

    @Override
    public void runSeeds() {
        new UserTableSeeder().run();
        new CompanyTableSeeder().run();
    }

    @Override
    public UserDao getDao() {
        return userDao;
    }

    public UserRoleDao getUserRoleDao() {
        return userRoleDao;
    }

    public CompanyDao getCompanyDao() {
        return companyDao;
    }

    public static UserModule getInstance() {
        if (USER_MODULE_INSTANCE == null) {
            synchronized (UserModule.class) {
                if (USER_MODULE_INSTANCE == null) {
                    USER_MODULE_INSTANCE = new UserModule();
                }
            }
        }

        return USER_MODULE_INSTANCE;
    }
}
