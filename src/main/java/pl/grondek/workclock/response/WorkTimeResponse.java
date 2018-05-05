package pl.grondek.workclock.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkTimeResponse {

    private DurationResponse workTime;

    private DurationResponse balance;
}
