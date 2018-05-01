package pl.grondek.workclock.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class DurationResponse {
    private long hours;

    private int minutes;
}
