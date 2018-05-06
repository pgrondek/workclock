package pl.grondek.workclock.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import pl.grondek.workclock.entity.EventEntity
import pl.grondek.workclock.entity.WorkDayEntity
import pl.grondek.workclock.model.EventType
import pl.grondek.workclock.test.TestMockConfiguration
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@ContextConfiguration(classes = [WorkTimeService, TestMockConfiguration])
class WorkTimeServiceTest extends Specification {
    @Autowired
    WorkTimeService workTimeService

    @SuppressWarnings("GroovyAccessibility")
    def "Check if time is calculated correctly with multiple days"() {
        given: "Two days spend time 8h"
        List<WorkDayEntity> events = [
            new WorkDayEntity(
                date: LocalDate.of(2018, 1, 2),
                events: [
                    new EventEntity(
                        time: LocalTime.of(8, 0),
                        type: EventType.PUNCH_IN
                    ),
                    new EventEntity(
                        time: LocalTime.of(16, 00),
                        type: EventType.PUNCH_OUT
                    )
                ]
            ),
            new WorkDayEntity(
                date: LocalDate.of(2018, 1, 3),
                events: [
                    new EventEntity(
                        time: LocalTime.of(8, 0),
                        type: EventType.PUNCH_IN
                    ),
                    new EventEntity(
                        time: LocalTime.of(16, 00),
                        type: EventType.PUNCH_OUT
                    )
                ]
            )
        ]

        when: "Run calculation"
        def duration = workTimeService.calculateTime(events)

        then: "Check if calculated time is 16h"
        duration == Duration.ofHours(16L)
    }

    @SuppressWarnings("GroovyAccessibility")
    def "Check if time is calculated correctly when still in work"() {
        given: "Punch in event 5 minutes ago"
        def events = new WorkDayEntity(
            date: LocalDate.now(),
            events: [
                new EventEntity(
                    time: LocalTime.now().minusMinutes(5L),
                    type: EventType.PUNCH_IN
                )
            ]
        )


        when: "Run calculation"
        def duration = workTimeService.calculateTime(events)

        then: "Check if calculated time is more or equal than 5 minutes"
        duration >= Duration.ofMinutes(5L)
    }
}
