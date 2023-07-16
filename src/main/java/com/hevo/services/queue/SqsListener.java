package com.hevo.services.queue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Polls and consumes messages according to the specified `SqsQueueConfig`.
 * Calls the `messageHandler` to handle the messsage.
 *
 * @param <T> The type of message to consume.
 */
@Slf4j
public class SqsListener<T> implements Runnable {

    private final AmazonSQS sqsClient;
    private final Handler<T> handler;
    private final SqsQueueConsumerConfig config;
    private final String queueUrl;
    private final ObjectMapper objectMapper;
    private final Class<T> messageClazz;

    public SqsListener(AmazonSQS sqsClient, SqsQueueConsumerConfig config, Handler<T> handler,
                       ObjectMapper mapper, Class<T> messageClazz) {
        this.sqsClient = sqsClient;
        this.config = config;
        this.handler = handler;
        this.queueUrl = sqsClient.getQueueUrl(config.getQueueName()).getQueueUrl();
        this.objectMapper = mapper;
        this.messageClazz = messageClazz;
    }

    @Override
    public void run() {
        log.trace("Polling for messages for queue: " + config.getQueueName());
        ReceiveMessageRequest receiveRequest =
                new ReceiveMessageRequest().withQueueUrl(queueUrl).withWaitTimeSeconds(config.getWaitTimeSeconds());

        List<Message> messages = sqsClient.receiveMessage(receiveRequest).getMessages();
        for (Message message : messages) {
            String messageBody = message.getBody();
            try {
                T messageObject;
                if (messageClazz.equals(String.class)) {
                    messageObject = (T) messageBody;
                } else {
                    messageObject = objectMapper.readValue(messageBody, messageClazz);
                }
                handler.handle(messageObject);
                sqsClient.deleteMessage(queueUrl, message.getReceiptHandle());
            } catch (Exception e) {
                log.error("Error processing message: ", e);
            }
        }
    }
}
