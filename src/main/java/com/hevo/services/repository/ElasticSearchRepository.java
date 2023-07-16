package com.hevo.services.repository;

import com.hevo.services.client.es.ElasticSearchClient;
import com.hevo.services.model.FileData;
import com.hevo.services.model.FileInfo;

import javax.inject.Inject;
import java.util.List;

public class ElasticSearchRepository implements FileRepository {
    private final ElasticSearchClient elasticSearchClient;

    @Inject
    public ElasticSearchRepository(ElasticSearchClient elasticSearchClient){
        this.elasticSearchClient = elasticSearchClient;
    }

    @Override
    public List<FileInfo> searchFile(String query) {
        return null;
    }

    @Override
    public void upsertFile(FileData fileData) {

    }
}
