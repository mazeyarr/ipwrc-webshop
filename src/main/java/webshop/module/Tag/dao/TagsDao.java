package webshop.module.Tag.dao;

import org.hibernate.SessionFactory;
import webshop.core.iinterface.DAO;
import webshop.module.Product.model.Product;
import webshop.module.Tag.model.Tag;

public class TagsDao extends DAO<Tag> {
    public TagsDao(SessionFactory factory) {
        super(factory);
    }
}
