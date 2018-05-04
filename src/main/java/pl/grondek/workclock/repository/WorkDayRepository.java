package pl.grondek.workclock.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import pl.grondek.workclock.entity.WorkDayEntity;

import java.time.LocalDate;

@Repository
public class WorkDayRepository extends AbstractRepository<WorkDayEntity, LocalDate> {
    public WorkDayRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
