package com.hevo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

public class FileSearchServiceModule extends DropwizardAwareModule<FileSearchServiceConfiguration> {
    private final ObjectMapper objectMapper;

    public FileSearchServiceModule(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toInstance(objectMapper);
        // Default access to dropwizard objects.
        configuration();
        environment();
        bootstrap();
    }

}
