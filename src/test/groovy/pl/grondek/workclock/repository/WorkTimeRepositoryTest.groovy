package pl.grondek.workclock.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import pl.grondek.workclock.entity.WorkTimeEntity
import pl.grondek.workclock.model.EventType
import pl.grondek.workclock.test.HibernateTestConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

@ContextConfiguration(classes = [HibernateTestConfiguration, WorkTimeRepository])
class WorkTimeRepositoryTest extends Specification {
    @Autowired
    WorkTimeRepository workTimeRepository

    void setup() {
        def entities = workTimeRepository.findAll()

        for (WorkTimeEntity entity : entities) {
            workTimeRepository.delete(entity)
        }
    }

    def "Check if findInRange returns element in range"() {
        given:
        def startTime = LocalDateTime.now().minusDays(1)
        def endTime = LocalDateTime.now().plusDays(1)
        workTimeRepository.save(new WorkTimeEntity(
            time: LocalDateTime.now(),
            type: EventType.PUNCH_IN
        ))

        when:
        def list = workTimeRepository.findInRange(startTime, endTime)

        then:
        list != null
        list.size() == 1
    }

    @Unroll
    def "Check if findInRange not returns element out of range"() {
        given:
        def startTime = LocalDateTime.of(2017, 1, 2, 0, 0)
        def endTime = LocalDateTime.of(2017, 1, 5, 0, 0)
        workTimeRepository.save(
            new WorkTimeEntity(
                time: eventTime,
                type: EventType.PUNCH_IN
            ))

        when:
        def list = workTimeRepository.findInRange(startTime, endTime)

        then:
        list != null
        list.size() == 0

        where:
        eventTime                            | _
        LocalDateTime.of(2017, 1, 6, 0, 0)   | _
        LocalDateTime.of(2017, 1, 5, 0, 1)   | _
        LocalDateTime.of(2017, 1, 1, 0, 0)   | _
        LocalDateTime.of(2017, 1, 1, 23, 59) | _
    }

    def "Check if findAfter returns element after date"() {
        given:
        def startTime = LocalDateTime.now().minusDays(1)
        workTimeRepository.save(new WorkTimeEntity(
            time: LocalDateTime.now(),
            type: EventType.PUNCH_IN
        ))

        when:
        def list = workTimeRepository.findAfter(startTime)

        then:
        list != null
        list.size() == 1
    }


    @Unroll
    def "Check if findAfter not returns element out of range"() {
        given:
        def startTime = LocalDateTime.of(2017, 1, 2, 0, 0)
        workTimeRepository.save(
            new WorkTimeEntity(
                time: eventTime,
                type: EventType.PUNCH_IN
            ))

        when:
        def list = workTimeRepository.findAfter(startTime)

        then:
        list != null
        list.size() == 0

        where:
        eventTime                            | _
        LocalDateTime.of(2017, 1, 1, 0, 0)   | _
        LocalDateTime.of(2017, 1, 1, 23, 59) | _
    }

    def "Check if findBefore returns element after date"() {
        given:
        def endTime = LocalDateTime.now().plusDays(1)
        workTimeRepository.save(new WorkTimeEntity(
            time: LocalDateTime.now(),
            type: EventType.PUNCH_IN
        ))

        when:
        def list = workTimeRepository.findBefore(endTime)

        then:
        list != null
        list.size() == 1
    }

    @Unroll
    def "Check if findBefore not returns element out of range"() {
        given:
        def endTime = LocalDateTime.of(2017, 1, 2, 0, 0)
        workTimeRepository.save(
            new WorkTimeEntity(
                time: eventTime,
                type: EventType.PUNCH_IN
            ))

        when:
        def list = workTimeRepository.findBefore(endTime)

        then:
        list != null
        list.size() == 0

        where:
        eventTime                          | _
        LocalDateTime.of(2017, 1, 6, 0, 0) | _
        LocalDateTime.of(2017, 1, 5, 0, 1) | _
    }

}
