package pl.grondek.workclock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.grondek.workclock.model.EventType;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TYPE = "type";

    @Id
    @Column(name = COLUMN_ID, unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = COLUMN_TIME, nullable = false)
    private LocalTime time;

    @Column(name = COLUMN_TYPE, nullable = false)
    private EventType type;
}
