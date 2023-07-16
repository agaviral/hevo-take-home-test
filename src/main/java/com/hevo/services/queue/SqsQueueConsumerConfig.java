package com.hevo.services.queue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class SqsQueueConsumerConfig {
    @NotEmpty
    private String queueName;

    @Min(1)
    private int pollingIntervalSeconds = 1;

    @Min(1)
    @Max(20)
    private int waitTimeSeconds = 20;

    @Min(1)
    private int numThreads = 1;
}
