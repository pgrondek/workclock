package pl.grondek.workclock.service;

import org.springframework.stereotype.Service;
import pl.grondek.workclock.entity.EventEntity;
import pl.grondek.workclock.entity.WorkDayEntity;
import pl.grondek.workclock.model.EventType;
import pl.grondek.workclock.model.WorkTime;
import pl.grondek.workclock.repository.EventRepository;
import pl.grondek.workclock.repository.WorkDayRepository;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkTimeService {

    private final WorkDayRepository workDayRepository;

    private final EventRepository eventRepository;

    public WorkTimeService(WorkDayRepository workDayRepository, EventRepository eventRepository) {
        this.workDayRepository = workDayRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void punchIn() {
        createEventNow(EventType.PUNCH_IN);
    }

    @Transactional
    public void punchOut() {
        createEventNow(EventType.PUNCH_OUT);
    }

    public List<WorkDayEntity> list() {
        return workDayRepository.findAll();
    }

    public WorkTime calculateAllTime() {
        final List<WorkDayEntity> allEvents = workDayRepository.findAll();
        final Duration duration = calculateTime(allEvents);
        final Duration balance = duration.minus(Duration.ofHours(8L).multipliedBy(allEvents.size()));

        final WorkTime workTime = WorkTime.builder()
            .workTime(duration)
            .balance(balance)
            .build();
        return workTime;
    }

    public WorkTime todayTime() {
        final LocalDate today = LocalDate.now();
        final WorkDayEntity dayEntity = workDayRepository.findById(today);

        if (dayEntity == null) {
            return null;
        }

        final Duration duration = calculateTime(dayEntity);
        final Duration balance = duration.minus(Duration.ofHours(8L));

        final WorkTime workTime = WorkTime.builder()
            .workTime(duration)
            .balance(balance)
            .build();
        return workTime;
    }

    private Duration calculateTime(Iterable<WorkDayEntity> workDayList) {
        Duration workTime = Duration.ZERO;

        for (WorkDayEntity dayEntity : workDayList) {
            final Duration dayDuration = calculateTime(dayEntity);
            workTime = workTime.plus(dayDuration);
        }

        return workTime;
    }

    private Duration calculateTime(WorkDayEntity workDayEntity) {
        Duration workTime = Duration.ZERO;
        EventEntity inEvent = null;
        EventEntity outEvent = null;

        for (EventEntity nextEvent : workDayEntity.getEvents()) {
            if (nextEvent.getType() == EventType.PUNCH_IN && inEvent == null) {
                inEvent = nextEvent;
                // Ignore out event when there is no in event
            } else if (nextEvent.getType() == EventType.PUNCH_OUT && inEvent != null) {
                outEvent = nextEvent;
            }

            if (inEvent != null && outEvent != null) {
                final LocalTime inTime = inEvent.getTime();
                final LocalTime outTime = outEvent.getTime();

                final Duration duration = Duration.between(inTime, outTime);
                workTime = workTime.plus(duration);

                // nullify references so they won't
                inEvent = null;
                outEvent = null;
            }
        }

        final LocalDate today = LocalDate.now();

        // In in work calculate work time until now
        if (inEvent != null && workDayEntity.getDate().equals(today)) {
            final LocalTime inTime = inEvent.getTime();
            final LocalTime outTime;

            // If there is no exit use end of day unless it's today, then use current time
            if (workDayEntity.getDate().equals(today)) {
                outTime = LocalTime.now();
            } else {
                outTime = LocalTime.MAX;
            }

            final Duration duration = Duration.between(inTime, outTime);
            workTime = workTime.plus(duration);
        }

        return workTime;
    }

    private void createEventNow(EventType eventType) {
        final LocalDate today = LocalDate.now();
        final LocalTime now = LocalTime.now();

        createEvent(eventType, today, now);
    }

    private void createEvent(EventType eventType, LocalDate date, LocalTime time) {
        WorkDayEntity workDay = getOrCreateWorkDayEntity(date);

        final EventEntity unsavedEvent = EventEntity.builder()
            .time(time)
            .type(eventType)
            .build();

        final Integer entityId = eventRepository.save(unsavedEvent);
        final EventEntity eventEntity = eventRepository.findById(entityId);

        final List<EventEntity> eventList = workDay.getEvents();
        eventList.add(eventEntity);
        workDayRepository.save(workDay);
    }

    private WorkDayEntity getOrCreateWorkDayEntity(LocalDate date) {
        WorkDayEntity workDay = workDayRepository.findById(date);
        if (workDay == null) {
            final WorkDayEntity newEntity = WorkDayEntity.builder()
                .date(date)
                .events(new ArrayList<>())
                .build();

            final LocalDate entityId = workDayRepository.save(newEntity);
            workDay = workDayRepository.findById(entityId);
        }

        return workDay;
    }
}
