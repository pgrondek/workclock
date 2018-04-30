package pl.grondek.workclock.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import pl.grondek.workclock.entity.WorkTimeEntity;

@Repository
public class WorkTimeRepository extends AbstractRepository<WorkTimeEntity> {
    public WorkTimeRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
