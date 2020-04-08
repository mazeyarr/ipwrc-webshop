package webshop.module.Product.service;

import webshop.core.iinterface.CoreValue;
import webshop.core.iinterface.Translator;
import webshop.module.Product.ProductModule;
import webshop.module.Product.dao.ProductDiscountsDao;
import webshop.module.Product.dao.ProductsDao;
import webshop.module.Product.exception.ProductNotFoundException;
import webshop.module.Product.model.Product;
import webshop.module.Product.model.ProductDiscount;

import java.util.List;

public class ProductService {
    public static Product createProduct(Product product) {
        getDao().create(product);

        if (
            product.hasDiscounts()
            &&
            product.getProductDiscounts().size() > CoreValue.EMPTY
        ) {
            product.getProductDiscounts().forEach(productDiscount -> {
                productDiscount.setProduct(product);
                if (productDiscount.getId() == CoreValue.UNSET_0) {
                    getDiscountDao().create(productDiscount);
                }
            });
        }

        return product;
    }

    public static ProductDiscount createProductDiscount(ProductDiscount productDiscount) {
        getDiscountDao().create(productDiscount);

        return productDiscount;
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

            if (product == CoreValue.UNSET_NULL) {
                throw new ProductNotFoundException(USER_NOT_FOUND_MESSAGE);
            }

            return product;
        } catch (Exception e) {
            throw new ProductNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    public static Product updateProduct(Product product) {
        getDao().update(product);

        return findProductById(product.getId());
    }

    public static Product tagProduct(long id, String tagName) {
        return new Product(); // TODO
    }

    public static boolean deleteProductById(long id) throws ProductNotFoundException {
        Product product = findOrFailProductById(id);

        deleteProduct(product);

        try {
            findOrFailProductById(id);

            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void deleteProduct(Product product) {
        getDao().delete(product);
    }

    private static ProductsDao getDao() {
        return ProductModule.getInstance().getDao();
    }

    private static ProductDiscountsDao getDiscountDao() {
        return ProductModule.getInstance().getDiscountsDao();
    }
}
