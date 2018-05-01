package pl.grondek.workclock.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import pl.grondek.workclock.entity.WorkTimeEntity
import pl.grondek.workclock.model.EventType
import pl.grondek.workclock.test.TestMockConfiguration
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDateTime

@ContextConfiguration(classes = [WorkTimeService, TestMockConfiguration])
class WorkTimeServiceTest extends Specification {
    @Autowired
    WorkTimeService workTimeService

    @SuppressWarnings("GroovyAccessibility")
    def "Check if time is calculated correctly with multiple days"() {
        given: "Two days spend time 8h"
        def events = [
            new WorkTimeEntity(
                time: LocalDateTime.of(2017, 1, 2, 8, 0),
                type: EventType.PUNCH_IN
            ),
            new WorkTimeEntity(
                time: LocalDateTime.of(2017, 1, 2, 16, 0),
                type: EventType.PUNCH_OUT
            ),
            new WorkTimeEntity(
                time: LocalDateTime.of(2017, 1, 3, 8, 0),
                type: EventType.PUNCH_IN
            ),
            new WorkTimeEntity(
                time: LocalDateTime.of(2017, 1, 3, 16, 0),
                type: EventType.PUNCH_OUT
            )
        ]

        when: "Run calculation"
        def duration = workTimeService.calculateTime(events.iterator())

        then: "Check if calculated time is 16h"
        duration == Duration.ofHours(16L)
    }

    @SuppressWarnings("GroovyAccessibility")
    def "Check if time is calculated correctly when still in work"() {
        given: "Punch in event 5 minutes ago"
        def events = [
            new WorkTimeEntity(
                time: LocalDateTime.now().minusMinutes(5L),
                type: EventType.PUNCH_IN
            )
        ]

        when: "Run calculation"
        def duration = workTimeService.calculateTime(events.iterator())

        then: "Check if calculated time is more or equal than 5 minutes"
        duration >= Duration.ofMinutes(5L)
    }
}
