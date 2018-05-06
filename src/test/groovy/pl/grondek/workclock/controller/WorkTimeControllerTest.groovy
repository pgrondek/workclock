package pl.grondek.workclock.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import pl.grondek.workclock.entity.EventEntity
import pl.grondek.workclock.entity.WorkDayEntity
import pl.grondek.workclock.model.EventType
import pl.grondek.workclock.repository.WorkDayRepository
import pl.grondek.workclock.response.WorkTimeResponse
import pl.grondek.workclock.service.WorkTimeService
import pl.grondek.workclock.test.TestMockConfiguration
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

@ContextConfiguration(classes = [WorkTimeController, WorkTimeService, TestMockConfiguration])
class WorkTimeControllerTest extends Specification {

    @Autowired
    WorkTimeController workTimeController

    @Autowired
    WorkDayRepository workDayRepository

    def "Check if duration and balance together gives 8h"() {
        given: "Given in/out time with seconds"
        workDayRepository.findAll() >> [
            new WorkDayEntity(
                date: LocalDate.of(2018, 1, 2),
                events: [
                        new EventEntity(
                            time: LocalTime.of(8,0, 30),
                            type: EventType.PUNCH_IN
                        ),
                        new EventEntity(
                            time: LocalTime.of(10,0),
                            type: EventType.PUNCH_OUT
                        )
                ]
            ),
        ]

        when: "Getting status of work time"
        WorkTimeResponse duration = workTimeController.duration()

        then: "Time spent plus left balance should ber 8h"
        def hours = -duration.balance.hours + duration.workTime.hours
        def minutes = duration.balance.minutes + duration.workTime.minutes
        if(minutes==60){
            hours++
            minutes = 0
        }

        assert hours == 8
        assert minutes == 0

    }

}
