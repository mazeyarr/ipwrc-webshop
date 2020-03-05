package webshop.module.Product;

import io.dropwizard.setup.Environment;
import webshop.module.Product.model.Product;
import webshop.module.User.UserModule;
import webshop.core.iinterface.WebshopModule;

public class ProductModule implements WebshopModule<Object> {
    private static volatile UserModule PRODUCT_MODULE_INSTANCE;

    public ProductModule() {
        mEntities.add(Product.class);
    }

    @Override
    public void init(Environment environment) {
        initResources(environment);
    }

    @Override
    public void initResources(Environment environment) {

    }

    @Override
    public Object getDao() {
        return null;
    }

    public static UserModule getInstance() {
        if (PRODUCT_MODULE_INSTANCE == null) {
            synchronized (UserModule.class) {
                if (PRODUCT_MODULE_INSTANCE == null) {
                    PRODUCT_MODULE_INSTANCE = new UserModule();
                }
            }
        }

        return PRODUCT_MODULE_INSTANCE;
    }
}
