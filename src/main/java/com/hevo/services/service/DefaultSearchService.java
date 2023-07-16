package com.hevo.services.service;

import com.hevo.services.repository.FileInfoSearchResponse;
import com.hevo.services.repository.FileRepository;
import com.hevo.services.resource.response.SearchResponse;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class DefaultSearchService implements SearchService {
    @Inject
    FileRepository fileRepository;

    @Override
    public SearchResponse search(String query) {
        log.info("Got query:" + query);
        FileInfoSearchResponse response = fileRepository.searchFile(query, 10, 0);
        return SearchResponse.builder()
                .files(response.getFileInfoList())
                .totalNumberOfResults(response.getTotalNumberOfResults())
                .build();
    }
}
