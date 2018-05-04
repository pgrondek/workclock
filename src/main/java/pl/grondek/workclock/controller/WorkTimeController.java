package pl.grondek.workclock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.grondek.workclock.entity.EventEntity;
import pl.grondek.workclock.entity.WorkDayEntity;
import pl.grondek.workclock.response.DurationResponse;
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

    @GetMapping("/in")
    public void punchIn() {
        workTimeService.punchIn();
    }

    @GetMapping("/out")
    public void punchOut() {
        workTimeService.punchOut();
    }

    @GetMapping("/list")
    public List<WorkTimeResponse> list() {
        final List<WorkDayEntity> workDayEntityList = workTimeService.list();
        final List<WorkTimeResponse> responseList = map(workDayEntityList);

        return responseList;
    }

    @GetMapping("/duration")
    public DurationResponse duration() {
        final Duration duration = workTimeService.calculateAllTime();
        return mapResponse(duration);
    }

    @GetMapping("/duration/today")
    public DurationResponse today() {
        final Duration duration = workTimeService.todayTime();
        return mapResponse(duration);
    }

    private DurationResponse mapResponse(Duration duration) {
        final long hours = duration.toHours();
        final int minutes = (int) (duration.toMinutes() - (hours * 60));

        return DurationResponse.builder()
            .hours(hours)
            .minutes(minutes)
            .build();
    }

    private List<WorkTimeResponse> map(Iterable<WorkDayEntity> workDayEntityList) {
        List<WorkTimeResponse> workTimeResponseList = new ArrayList<>();
        for (WorkDayEntity dayEntity : workDayEntityList) {
            for (EventEntity eventEntity : dayEntity.getEvents()) {
                final WorkTimeResponse response = WorkTimeResponse.builder()
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
