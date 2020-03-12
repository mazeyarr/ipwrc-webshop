package webshop.module.Product.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import webshop.core.iinterface.DAO;
import webshop.module.Product.model.Product;
import webshop.module.User.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ProductsDao extends DAO<Product> {
    public ProductsDao(SessionFactory factory) {
        super(factory);
    }
}
