package webshop.module.User.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import webshop.core.iinterface.DAO;
import webshop.module.User.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserDao extends DAO<User> {
    public UserDao(SessionFactory factory) {
        super(factory);
    }

    public User find(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(root).where(
                criteriaBuilder.equal(root.get("email"), username)
        );

        Query<User> query = currentSession().createQuery(criteriaQuery);

        return query.getSingleResult();
    }
}
