package com.hevo.services.queue;

// Generic handler for any kind of message that SqS consumer consumes.
public interface Handler<T> {
    void handle(T message) throws Exception;
}
