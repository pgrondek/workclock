package pl.grondek.workclock.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import pl.grondek.workclock.entity.WorkTimeEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class WorkTimeRepository extends AbstractRepository<WorkTimeEntity> {
    public WorkTimeRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional
    public List<WorkTimeEntity> findInRange(LocalDateTime start, LocalDateTime end) {
        final Session sessionFactory = getSession();
        final CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        final CriteriaQuery<WorkTimeEntity> criteriaQuery = criteriaBuilder.createQuery(persistentClass);
        final Root<WorkTimeEntity> workTimeRoot = criteriaQuery.from(persistentClass);
        criteriaQuery.where(criteriaBuilder.and(
            criteriaBuilder.greaterThanOrEqualTo(workTimeRoot.get(WorkTimeEntity.COLUMN_TIME), start),
            criteriaBuilder.lessThanOrEqualTo(workTimeRoot.get(WorkTimeEntity.COLUMN_TIME), end)
        ));

        return sessionFactory.createQuery(criteriaQuery)
            .getResultList();
    }

    @Transactional
    public List<WorkTimeEntity> findAfter(LocalDateTime time) {
        final Session sessionFactory = getSession();
        final CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        final CriteriaQuery<WorkTimeEntity> criteriaQuery = criteriaBuilder.createQuery(persistentClass);
        final Root<WorkTimeEntity> workTimeRoot = criteriaQuery.from(persistentClass);
        criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(workTimeRoot.get(WorkTimeEntity.COLUMN_TIME), time));

        return sessionFactory.createQuery(criteriaQuery)
            .getResultList();
    }

    @Transactional
    public List<WorkTimeEntity> findBefore(LocalDateTime time) {
        final Session sessionFactory = getSession();
        final CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        final CriteriaQuery<WorkTimeEntity> criteriaQuery = criteriaBuilder.createQuery(persistentClass);
        final Root<WorkTimeEntity> workTimeRoot = criteriaQuery.from(persistentClass);
        criteriaQuery.where(criteriaBuilder.lessThanOrEqualTo(workTimeRoot.get(WorkTimeEntity.COLUMN_TIME), time));

        return sessionFactory.createQuery(criteriaQuery)
            .getResultList();
    }
}
