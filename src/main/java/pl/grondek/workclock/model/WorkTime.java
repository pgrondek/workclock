package pl.grondek.workclock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Builder
@AllArgsConstructor
@Data
public class WorkTime {
    private Duration workTime;

    private Duration balance;
}
