package pl.grondek.workclock.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.grondek.workclock.model.EventType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkTimeResponse {

    private int id;

    private LocalDateTime time;

    private EventType type;
}
