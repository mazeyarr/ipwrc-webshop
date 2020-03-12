package webshop.module.Product.service;

import webshop.core.iinterface.Translator;
import webshop.module.Product.ProductModule;
import webshop.module.Product.dao.ProductDiscountsDao;
import webshop.module.Product.dao.ProductsDao;
import webshop.module.Product.exception.ProductNotFoundException;
import webshop.module.Product.model.Product;

import java.util.List;

public class ProductService {
    public static Product createProduct(Product product) {
        getDao().create(product);

        return product;
    }

    public static List<Product> getAllProducts() {
        return getDao().all(Product.class);
    }

    public static Product findProductById(long id) {
        return getDao().find(id);
    }

    public static Product findOrFailProductById(long id) throws ProductNotFoundException {
        final String USER_NOT_FOUND_MESSAGE = Translator.translate(
                "Product id: " + id + " not found"
        );

        try {
            Product product = findProductById(id);

            if (product == null) {
                throw new ProductNotFoundException(USER_NOT_FOUND_MESSAGE);
            }

            return product;
        } catch (Exception e) {
            throw new ProductNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    public static Product updateProduct(Product product) throws ProductNotFoundException {
        return new Product();
    }

    public static boolean deleteProductById(long id) throws ProductNotFoundException {
        return false;
    }

    public static void deleteProduct(Product product) {
    }

    private static ProductsDao getDao() {
        return ProductModule.getInstance().getDao();
    }

    private static ProductDiscountsDao getDiscountDao() {
        return ProductModule.getInstance().getDiscountsDao();
    }
}
