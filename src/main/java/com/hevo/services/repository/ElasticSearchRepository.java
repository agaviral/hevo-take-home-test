package com.hevo.services.repository;

import com.hevo.services.client.es.ElasticSearchClient;
import com.hevo.services.client.es.GenericResponse;
import com.hevo.services.client.es.QueryFileIndexResponse;
import com.hevo.services.entity.FileInfoDocument;
import com.hevo.services.model.FileData;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ElasticSearchRepository implements FileRepository {
    private final ElasticSearchClient elasticSearchClient;

    @Inject
    public ElasticSearchRepository(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    @Override
    public FileInfoSearchResponse searchFile(String query, int limit, int offset) throws IOException {
        QueryFileIndexResponse response = elasticSearchClient.queryFileInfo(query, limit, offset);
        return FileInfoSearchResponse.builder()
                .fileInfoList(response.getFiles().stream()
                        .map(f -> FileInfoSearchResponse.FileInfo.builder()
                                .url(f.getUrl())
                                .modifiedAt(f.getModifiedAt())
                                .build())
                        .collect(Collectors.toList()))
                .totalNumberOfResults(response.getTotalNumberOfResults())
                .build();
    }

    @Override
    public void upsertFile(FileData fileData) throws IOException {
        Optional<GenericResponse<FileInfoDocument>> fileDocument =
                elasticSearchClient.getFileInfo(fileData.getUrl());
        if (fileDocument.isPresent()) {
            if (fileDocument.get().getDocument().getModifiedAt().isBefore(fileData.getModifiedAt())) {
                elasticSearchClient.updateFileInfo(
                        FileInfoDocument.from(fileData),
                        fileDocument.get().getPrimaryTerm(),
                        fileDocument.get().getSequenceNumber());
            }
        } else {
            elasticSearchClient.insertFileInfo(FileInfoDocument.from(fileData));
        }
    }

    @Override
    public void deleteFile(String url) throws IOException {
        elasticSearchClient.delete(url);
    }
}
