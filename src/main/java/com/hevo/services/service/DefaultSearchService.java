package com.hevo.services.service;

import com.hevo.services.datasource.Datasource;
import com.hevo.services.model.FileData;
import com.hevo.services.model.FileInfo;
import com.hevo.services.parser.FileParser;
import com.hevo.services.parser.FileParserSelector;
import com.hevo.services.parser.ParserNotFoundException;
import com.hevo.services.repository.FileInfoSearchResponse;
import com.hevo.services.repository.FileRepository;
import com.hevo.services.resource.response.SearchResponse;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Singleton
public class DefaultSearchService implements SearchService {
    @Inject
    FileRepository fileRepository;

    @Inject
    Datasource datasource;

    @Inject
    FileParserSelector fileParserSelector;

    @Override
    public SearchResponse search(String query, int limit, int offset) {
        FileInfoSearchResponse response = null;
        try {
            response = fileRepository.searchFile(query, limit, offset);
        } catch (IOException e) {
            log.error("Unable to search index", e);
            throw new InternalServerErrorException("Unable to search index");
        }

        List<SearchResponse.FileInfo> result = response.getFileInfoList().stream()
                .map(f -> SearchResponse.FileInfo.builder()
                        .url(f.getUrl())
                        .modifiedAt(f.getModifiedAt().toString())
                        .build())
                .toList();

        return SearchResponse.builder()
                .files(result)
                .numResults(result.size())
                .totalNumberOfResults(response.getTotalNumberOfResults())
                .build();
    }

    @Override
    public void readFromDatasourceAndIndex(FileInfo file) throws IOException {
        String content = "";
        try (InputStream fileData = datasource.readFile(file.getKey())) {
            FileParser parser = fileParserSelector.getFileParser(file.getKey());
            content = parser.getContent(fileData);
        } catch (IOException | ParserNotFoundException e) {
            log.error("Error parsing file: " + file.getUrl());
            throw new IOException("Error parsing file");
        }

        fileRepository.upsertFile(
                FileData.builder()
                        .url(file.getUrl())
                        .modifiedAt(file.getModifiedAt())
                        .content(content)
                        .build()
        );
    }

    @Override
    public void removeFromIndex(FileInfo fileInfo) {
        try {
            fileRepository.deleteFile(fileInfo.getUrl());
        } catch (IOException e) {
            log.error("Unable to remove from index", e);
            throw new InternalServerErrorException("Unable to remove from index");
        }
    }
}
