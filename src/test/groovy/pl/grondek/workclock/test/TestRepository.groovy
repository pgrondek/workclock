package pl.grondek.workclock.test

import org.hibernate.SessionFactory
import org.springframework.stereotype.Repository
import pl.grondek.workclock.repository.AbstractRepository

@Repository
class TestRepository extends AbstractRepository<TestEntity> {

    TestRepository(SessionFactory sessionFactory) {
        super(sessionFactory)
    }
}
