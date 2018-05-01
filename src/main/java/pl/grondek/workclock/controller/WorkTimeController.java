package pl.grondek.workclock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.grondek.workclock.response.DurationResponse;
import pl.grondek.workclock.response.WorkTimeResponse;
import pl.grondek.workclock.service.WorkTimeService;

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
        return workTimeService.list();
    }

    @GetMapping("/duration")
    public DurationResponse duration() {
        return workTimeService.calculateAllTime();
    }
}
