package webshop.core.iinterface;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import webshop.module.User.model.User;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class DAO<Entity extends MyEntity> extends AbstractDAO<Entity> {

    public DAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Entity create(Entity entity) {
        return persist(entity);
    }

    public Entity find(Long id) {
        Transaction transaction = null;
        Entity entity = null;

        try (Session session = currentSession().getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            entity = get(id);

            transaction.commit();
        } catch (Exception e) {
            rollbackTransactionIfNotNull(transaction);
        }

        return entity;
    }

    public List<User> all() {
        CriteriaBuilder cb = currentSession().getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> rootEntry = cq.from(User.class);
        CriteriaQuery<User> all = cq.select(rootEntry);

        TypedQuery<User> allQuery = currentSession().createQuery(all);
        return allQuery.getResultList();
    }

    public Entity update(Entity entity) {
        Transaction transaction = null;

        try(Session session = currentSession().getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            currentSession().merge(entity);
            currentSession().flush();

            transaction.commit();
        } catch (Exception exception) {
            rollbackTransactionIfNotNull(transaction);
        }

        return entity;
    }

    public void delete(Entity entity) {
        currentSession().delete(entity);
        currentSession().flush();
    }

    private static void rollbackTransactionIfNotNull(Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
        }
    }
}
