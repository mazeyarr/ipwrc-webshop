package webshop.module.Product.dao;

import org.hibernate.SessionFactory;
import webshop.core.iinterface.DAO;
import webshop.module.Product.model.Product;
import webshop.module.Product.model.ProductDiscount;

public class ProductDiscountsDao extends DAO<ProductDiscount> {
    public ProductDiscountsDao(SessionFactory factory) {
        super(factory);
    }
}
