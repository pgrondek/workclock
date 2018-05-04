package pl.grondek.workclock.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import pl.grondek.workclock.entity.EventEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

@Repository
public class EventRepository extends AbstractRepository<EventEntity, Integer> {
    public EventRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<EventEntity> findByDate(LocalDate date) {
        final Session sessionFactory = getSession();
        final CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        final CriteriaQuery<EventEntity> criteriaQuery = criteriaBuilder.createQuery(persistentClass);
        final Root<EventEntity> eventRoot = criteriaQuery.from(persistentClass);

        criteriaQuery.where(criteriaBuilder.equal(eventRoot.get(EventEntity.COLUMN_TIME), date));

        return sessionFactory.createQuery(criteriaQuery)
            .getResultList();
    }
}
