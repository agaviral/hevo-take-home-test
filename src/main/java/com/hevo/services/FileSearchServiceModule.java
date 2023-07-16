package com.hevo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hevo.services.repository.ElasticSearchRepository;
import com.hevo.services.repository.FileRepository;
import com.hevo.services.service.DefaultSearchService;
import com.hevo.services.service.SearchService;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

public class FileSearchServiceModule extends DropwizardAwareModule<FileSearchServiceConfiguration> {
    private final ObjectMapper objectMapper;

    public FileSearchServiceModule(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toInstance(objectMapper);
        bind(SearchService.class).to(DefaultSearchService.class);
        bind(FileRepository.class).to(ElasticSearchRepository.class);
        // Default access to dropwizard objects.
        configuration();
        environment();
        bootstrap();
    }


}
