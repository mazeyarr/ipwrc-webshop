package webshop;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.AmazonTranslateClientBuilder;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.cdimascio.dotenv.Dotenv;
import webshop.core.iinterface.UploadPaths;
import webshop.core.service.Amazon.AmazonCredentials;
import webshop.core.service.CoreService;

import javax.swing.plaf.synth.Region;

public class WebshopApplication extends Application<WebshopConfiguration> {
    private static final String APP_NAME = "IPWRC - WEBSHOP";
    private static final String DATABASE_MIGRATION_FILENAME = "webshop_migrations.xml";

    public static void main(final String[] args) throws Exception {
        new WebshopApplication().run(args);
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    private final HibernateBundle<WebshopConfiguration> hibernateBundle = new HibernateBundle<WebshopConfiguration>(
            CoreService.getInstance().getListOfModuleEntities(),
            new SessionFactoryFactory()
    ) {
        @Override
        public DataSourceFactory getDataSourceFactory(WebshopConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    private final MigrationsBundle<WebshopConfiguration> migrationsBundle = new MigrationsBundle<WebshopConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(WebshopConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }

        @Override
        public String getMigrationsFileName() {
            return DATABASE_MIGRATION_FILENAME;
        }
    };

    @Override
    public void initialize(final Bootstrap<WebshopConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(migrationsBundle);
    }

    @Override
    public void run(final WebshopConfiguration configuration,
                    final Environment environment) {
        CoreService.getInstance().initCore(configuration, environment, hibernateBundle);
        CoreService.getInstance().initModules(environment);
    }

}
