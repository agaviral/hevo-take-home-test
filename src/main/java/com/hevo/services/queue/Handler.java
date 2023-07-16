package com.hevo.services.queue;

public interface Handler<T> {
    void handle(T message) throws Exception;
}
