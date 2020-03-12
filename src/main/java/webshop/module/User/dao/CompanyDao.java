package webshop.module.User.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import webshop.core.iinterface.DAO;
import webshop.module.User.model.Company;
import webshop.module.User.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class CompanyDao extends DAO<Company> {
    public CompanyDao(SessionFactory factory) {
        super(factory);
    }

    public Company find(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();

        CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
        Root<Company> root = criteriaQuery.from(Company.class);

        criteriaQuery.select(root).where(
                criteriaBuilder.equal(root.get("email"), username)
        );

        Query<Company> query = currentSession().createQuery(criteriaQuery);

        return query.getSingleResult();
    }
}
