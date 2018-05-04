package pl.grondek.workclock.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Repository
public abstract class AbstractRepository<T, ID extends Serializable> {

    protected Class<T> persistentClass;

    protected Class<ID> idClass;

    private final SessionFactory sessionFactory;

    public AbstractRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        initPersistentClass();
    }

    private void initPersistentClass() {
        final Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractRepository.class);

        this.persistentClass = (Class<T>) classes[0];
        this.idClass = (Class<ID>) classes[1];
    }

    @Transactional
    public ID save(T element) {
        final ID id = (ID) getSession()
            .save(element);

        return id;
    }

    @Transactional
    public List<T> findAll() {
        Session sessionFactory = getSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(persistentClass);
        criteriaQuery.from(persistentClass);

        return sessionFactory.createQuery(criteriaQuery)
            .getResultList();
    }

    @Transactional
    public T findById(ID id) {
        final T entity = getSession().get(persistentClass, id);

        return entity;
    }

    @Transactional
    public void delete(T element) {
        getSession()
            .delete(element);
    }

    protected final Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
