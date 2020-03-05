package webshop.module.User;

import io.dropwizard.setup.Environment;
import webshop.core.service.CoreService;
import webshop.filter.AuthTokenFilter;
import webshop.interceptor.LocaleInterceptor;
import webshop.module.User.dao.UserDao;
import webshop.module.User.dao.UserRoleDao;
import webshop.module.User.model.User;
import webshop.module.User.model.UserRole;
import webshop.module.User.resource.AuthResource;
import webshop.module.User.resource.UserResource;
import webshop.core.iinterface.WebshopModule;

public class UserModule implements WebshopModule<UserDao> {
    private static volatile UserModule USER_MODULE_INSTANCE;
    private UserDao userDao;
    private UserRoleDao userRoleDao;

    public UserModule() {
        addEntityToModule(User.class);
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
        environment.jersey().register(new AuthResource());
    }

    private void initDao() {
        userDao = new UserDao(
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
    public UserDao getDao() {
        return userDao;
    }

    public UserRoleDao getUserRoleDao() {
        return userRoleDao;
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
