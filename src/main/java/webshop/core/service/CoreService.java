package webshop.core.service;

import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import io.github.cdimascio.dotenv.Dotenv;
import org.jdbi.v3.core.Jdbi;
import webshop.WebshopConfiguration;
import webshop.core.iinterface.UploadPaths;
import webshop.core.service.Amazon.AmazonCredentials;
import webshop.module.Product.ProductModule;
import webshop.module.User.UserModule;
import webshop.type.EnvironmentType;

import java.util.ArrayList;
import java.util.List;

public class CoreService {
    private static volatile CoreService CORE_SERVICE_INSTANCE;

    public static final Dotenv env = Dotenv.configure()
            .directory(UploadPaths.LOCAL_RESOURCE_FOLDER_PATH)
            .load();
    private final JdbiFactory mFactory = new JdbiFactory();
    private Jdbi mJdbi;
    private HibernateBundle<WebshopConfiguration> mHibernate;

    public void initCore(final WebshopConfiguration configuration,
                         final Environment environment,
                         HibernateBundle<WebshopConfiguration> hibernate) {
        if (getJdbi() == null) initDatabase(configuration, environment);
        this.mHibernate = hibernate;
    }


    public void initDatabase(WebshopConfiguration configuration, Environment environment) {
        this.setJdbi(
                getFactory().build(
                        environment,
                        configuration.getDataSourceFactory(), "postgresql"
                )
        );
    }

    public void initModules(Environment environment) {
        UserModule.getInstance().init(environment);
        ProductModule.getInstance().init(environment);
    }

    public void runSeeders() {
        UserModule.getInstance().runSeeds();
    }

    public List<Class<?>> getListOfModuleEntities() {
        List<Class<?>> listOfAllEntities = new ArrayList<>();

        listOfAllEntities.addAll(UserModule.getInstance().getEntities());
        listOfAllEntities.addAll(ProductModule.getInstance().getEntities());

        return listOfAllEntities;
    }

    public static EnvironmentType getEnvironmentMode() {
        return EnvironmentType.valueOf(getEnv().get("ENV"));
    }

    public static boolean isTranslationServiceOn() {
        return Boolean.parseBoolean(getEnv().get("AWS_TRANSLATE"));
    }

    public static Dotenv getEnv() {
        return env;
    }

    public JdbiFactory getFactory() {
        return mFactory;
    }

    public Jdbi getJdbi() {
        return mJdbi;
    }

    private void setJdbi(Jdbi mJdbi) {
        this.mJdbi = mJdbi;
    }

    public HibernateBundle<WebshopConfiguration> getHibernate() {
        return mHibernate;
    }

    public void setHibernate(HibernateBundle<WebshopConfiguration> hibernateConfig) {
        this.mHibernate = hibernateConfig;
    }

    public static CoreService getInstance() {
        if (CORE_SERVICE_INSTANCE == null) {
            synchronized (CoreService.class) {
                if (CORE_SERVICE_INSTANCE == null) {
                    CORE_SERVICE_INSTANCE = new CoreService();
                }
            }
        }

        return CORE_SERVICE_INSTANCE;
    }
}
