package pl.grondek.workclock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkDayEntity {
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_EVENTS = "events";

    @Id
    @Column(name = COLUMN_DATE, unique = true, nullable = false)
    private LocalDate date;

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = COLUMN_EVENTS)
    private List<EventEntity> events;
}
