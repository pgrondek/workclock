package pl.grondek.workclock.entity;

import lombok.Data;
import pl.grondek.workclock.model.PunchType;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class PuchinEntity {

    @Id
    private int id;

    private LocalDateTime time;

    private PunchType type;
}
