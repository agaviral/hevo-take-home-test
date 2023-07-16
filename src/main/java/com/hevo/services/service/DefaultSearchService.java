package com.hevo.services.service;

import com.hevo.services.resource.response.SearchResponse;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class DefaultSearchService implements SearchService {

    @Override
    public SearchResponse search(String query) {
        log.info("Got query:" + query);
        return null;
    }
}
