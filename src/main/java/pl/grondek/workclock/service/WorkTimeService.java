package pl.grondek.workclock.service;

import org.springframework.stereotype.Service;
import pl.grondek.workclock.entity.WorkTimeEntity;
import pl.grondek.workclock.model.EventType;
import pl.grondek.workclock.repository.WorkTimeRepository;
import pl.grondek.workclock.response.WorkTimeResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
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

    public Duration calculateAllTime() {
        final List<WorkTimeEntity> allEvents = workTimeRepository.findAll();

        return calculateTime(allEvents.iterator());
    }

    private Duration calculateTime(Iterator<WorkTimeEntity> eventIterator) {
        WorkTimeEntity inEvent = null;
        WorkTimeEntity outEvent = null;

        Duration workTime = Duration.ZERO;

        while (eventIterator.hasNext()) {
            final WorkTimeEntity nextEvent = eventIterator.next();

            if (nextEvent.getType() == EventType.PUNCH_IN && inEvent == null) {
                inEvent = nextEvent;
            } else if (nextEvent.getType() == EventType.PUNCH_OUT) {
                outEvent = nextEvent;
            }

            if (inEvent != null && outEvent != null) {
                final LocalDateTime inTime = inEvent.getTime();
                final LocalDateTime outTime = outEvent.getTime();

                final Duration duration = Duration.between(inTime, outTime);
                workTime = workTime.plus(duration);
            }
        }

        return workTime;
    }

    private void saveEvent(EventType eventType) {
        final WorkTimeEntity entity = WorkTimeEntity.builder()
            .time(LocalDateTime.now())
            .type(eventType)
            .build();

        workTimeRepository.save(entity);
    }
}
