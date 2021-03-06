package pl.grondek.workclock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.grondek.workclock.entity.EventEntity;
import pl.grondek.workclock.entity.WorkDayEntity;
import pl.grondek.workclock.model.Status;
import pl.grondek.workclock.model.WorkTime;
import pl.grondek.workclock.response.DurationResponse;
import pl.grondek.workclock.response.EventsResponse;
import pl.grondek.workclock.response.StatusResponse;
import pl.grondek.workclock.response.WorkTimeResponse;
import pl.grondek.workclock.service.WorkTimeService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/time")
public class WorkTimeController {

    private final WorkTimeService workTimeService;

    public WorkTimeController(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }

    @PostMapping("/in")
    public void punchIn() {
        workTimeService.punchIn();
    }

    @PostMapping("/out")
    public void punchOut() {
        workTimeService.punchOut();
    }

    @GetMapping("/list")
    public List<EventsResponse> list() {
        final List<WorkDayEntity> workDayEntityList = workTimeService.list();
        final List<EventsResponse> responseList = map(workDayEntityList);

        return responseList;
    }

    @GetMapping("/status")
    public StatusResponse status() {
        final Status status = workTimeService.status();
        return mapResponse(status);
    }

    @GetMapping("/duration/today")
    public WorkTimeResponse today() {
        final WorkTime workTime = workTimeService.todayTime();
        return mapResponse(workTime);
    }

    private DurationResponse mapResponse(Duration duration, boolean halfUp) {
        final long hours = duration.toHours();
        int minutes = (int) (duration.toMinutes() - (hours * 60));
        final long seconds = duration.getSeconds();

        if (seconds > 30) {
            minutes++;
        } else if (seconds == 30 && halfUp) {
            minutes++;
        }

        return DurationResponse.builder()
            .hours(hours)
            .minutes(minutes)
            .build();
    }

    private WorkTimeResponse mapResponse(WorkTime workTime) {
        return WorkTimeResponse.builder()
            .workTime(mapResponse(workTime.getWorkTime(), false))
            .balance(mapResponse(workTime.getBalance(), true))
            .build();
    }

    private StatusResponse mapResponse(Status status) {
        return StatusResponse.builder()
            .balance(mapResponse(status.getBalance(), false))
            .daysToReclaim(status.getDaysToReclaim())
            .build();
    }

    private List<EventsResponse> map(Iterable<WorkDayEntity> workDayEntityList) {
        List<EventsResponse> workTimeResponseList = new ArrayList<>();
        for (WorkDayEntity dayEntity : workDayEntityList) {
            for (EventEntity eventEntity : dayEntity.getEvents()) {
                final EventsResponse response = EventsResponse.builder()
                    .time(LocalDateTime.of(dayEntity.getDate(), eventEntity.getTime()))
                    .type(eventEntity.getType())
                    .id(eventEntity.getId())
                    .build();

                workTimeResponseList.add(response);
            }
        }

        return workTimeResponseList;
    }
}
