package com.hevo.services.queue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A simple consumer which start `n` `SqsListener` to consume messages.
 *
 * @param <T>
 */
@Slf4j
public class SqsConsumer<T> implements Managed {
    private final ScheduledExecutorService scheduledExecutorService;
    private final SqsQueueConsumerConfig config;
    private final AmazonSQS amazonSQS;
    private final ObjectMapper objectMapper;
    private final Handler<T> handler;
    private final Class<T> messageClazz;

    private final List<ScheduledFuture<?>> threads;

    public SqsConsumer(SqsQueueConsumerConfig config, Handler<T> handler, ObjectMapper mapper, Class<T> messageClazz) {
        this.config = config;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(config.getNumThreads());
        this.amazonSQS = AmazonSQSClientBuilder.defaultClient();
        this.handler = handler;
        this.objectMapper = mapper;
        this.threads = new ArrayList<>();
        this.messageClazz = messageClazz;
    }

    @Override
    public void start() throws Exception {
        log.info("Starting consumer for: " + config.getQueueName());
        for (int i = 0; i < config.getNumThreads(); i++) {
            ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                    new SqsListener<T>(amazonSQS, config, handler, objectMapper, messageClazz),
                    0,
                    config.getPollingIntervalSeconds(),
                    TimeUnit.SECONDS);

            threads.add(scheduledFuture);
        }
    }

    @Override
    public void stop() throws Exception {
        log.info("Shutting down consumer for: " + config.getQueueName());
        for (ScheduledFuture<?> thread : threads) {
            thread.cancel(false);
        }
        scheduledExecutorService.shutdown();
    }
}
