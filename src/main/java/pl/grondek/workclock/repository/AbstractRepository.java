package pl.grondek.workclock.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public abstract class AbstractRepository<T> {

    protected Class<T> persistentClass;

    private final SessionFactory sessionFactory;

    public AbstractRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        initPersistentClass();
    }

    private void initPersistentClass() {
        this.persistentClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractRepository.class);
    }

    @Transactional
    public void save(T element) {
        getSession()
                .save(element);
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


    protected final Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
