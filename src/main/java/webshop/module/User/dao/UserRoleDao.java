package webshop.module.User.dao;

import org.hibernate.SessionFactory;
import webshop.core.iinterface.DAO;
import webshop.module.User.model.UserRole;

public class UserRoleDao extends DAO<UserRole> {
    public UserRoleDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
