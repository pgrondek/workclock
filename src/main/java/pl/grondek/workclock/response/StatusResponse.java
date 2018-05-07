package pl.grondek.workclock.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class StatusResponse {
    private DurationResponse balance;

    private int daysToReclaim;
}
