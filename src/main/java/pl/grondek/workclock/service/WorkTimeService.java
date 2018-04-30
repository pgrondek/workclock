package pl.grondek.workclock.service;

import org.springframework.stereotype.Service;
import pl.grondek.workclock.entity.WorkTimeEntity;
import pl.grondek.workclock.model.EventType;
import pl.grondek.workclock.repository.WorkTimeRepository;
import pl.grondek.workclock.response.WorkTimeResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkTimeService {

    private final WorkTimeRepository workTimeRepository;

    public WorkTimeService(WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
    }

    public void punchIn() {
        saveEvent(EventType.PUNCH_IN);
    }

    public void punchOut() {
        saveEvent(EventType.PUNCH_OUT);
    }

    public List<WorkTimeResponse> list() {
        return workTimeRepository.findAll()
            .stream()
            .map(entity -> WorkTimeResponse.builder()
                .id(entity.getId())
                .time(entity.getTime())
                .type(entity.getType())
                .build()
            ).collect(Collectors.toList());
    }

    private void saveEvent(EventType eventType) {
        final WorkTimeEntity entity = WorkTimeEntity.builder()
            .time(LocalDateTime.now())
            .type(eventType)
            .build();

        workTimeRepository.save(entity);
    }
}
