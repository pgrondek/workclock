package pl.grondek.workclock.repository

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import pl.grondek.workclock.test.HibernateTestConfiguration
import pl.grondek.workclock.test.TestEntity
import pl.grondek.workclock.test.TestRepository
import spock.lang.Specification

@ContextConfiguration(classes = [HibernateTestConfiguration, TestRepository])
class AbstractRepositoryTest extends Specification {

    @Autowired
    SessionFactory sessionFactory

    @Autowired
    TestRepository testRepository

    def "Check save"() {
        given:
        def testEntity = new TestEntity()

        when:
        def list = testRepository.save(testEntity)

        then:
        notThrown Exception
    }

    def "Check findAll"() {
        when:
        def list = testRepository.findAll()

        then:
        list != null
    }
}
