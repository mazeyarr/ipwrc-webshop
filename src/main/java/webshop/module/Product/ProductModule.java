package webshop.module.Product;

import io.dropwizard.setup.Environment;
import webshop.core.iinterface.CoreModule;
import webshop.core.service.CoreService;
import webshop.module.Product.dao.ProductDiscountsDao;
import webshop.module.Product.dao.ProductsDao;
import webshop.module.Product.model.Product;
import webshop.module.Product.model.ProductDiscount;
import webshop.module.Product.resource.ProductResource;

public class ProductModule extends CoreModule<ProductsDao> {
    private static volatile ProductModule PRODUCT_MODULE_INSTANCE;

    private ProductsDao mProductDao;
    private ProductDiscountsDao mProductDiscountsDao;

    public ProductModule() {
        addEntityToModule(Product.class);
        addEntityToModule(ProductDiscount.class);
    }

    @Override
    public void init(Environment environment) {
        initDao();
        initResources(environment);
    }

    @Override
    public void initResources(Environment environment) {
        environment.jersey().register(new ProductResource());
    }

    @Override
    public void initDao() {
        mProductDao = new ProductsDao(
                CoreService.getInstance()
                        .getHibernate()
                        .getSessionFactory()
        );

        mProductDiscountsDao = new ProductDiscountsDao(
                CoreService.getInstance()
                        .getHibernate()
                        .getSessionFactory()
        );
    }

    @Override
    public ProductsDao getDao() {
        return mProductDao;
    }

    public ProductDiscountsDao getDiscountsDao() {
        return mProductDiscountsDao;
    }

    public static ProductModule getInstance() {
        if (PRODUCT_MODULE_INSTANCE == null) {
            synchronized (ProductModule.class) {
                if (PRODUCT_MODULE_INSTANCE == null) {
                    PRODUCT_MODULE_INSTANCE = new ProductModule();
                }
            }
        }

        return PRODUCT_MODULE_INSTANCE;
    }
}
