package pl.grondek.workclock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.grondek.workclock.model.EventType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkTimeEntity {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_EVENT_TYPE = "eventType";

    @Id
    @Column(name = COLUMN_ID, unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = COLUMN_TIME, unique = true, nullable = false)
    private LocalDateTime time;

    @Column(name = COLUMN_EVENT_TYPE, nullable = false)
    private EventType type;
}
