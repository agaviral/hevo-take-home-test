package com.hevo.services.task;

import com.hevo.services.datasource.Datasource;
import com.hevo.services.datasource.FileMetadata;
import com.hevo.services.model.FileInfo;
import com.hevo.services.service.SearchService;
import io.dropwizard.servlets.tasks.Task;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Admin task which helps in backfilling the index.
 * Reads all the files from the datasource and upserts them into the index.
 */
@Slf4j
@Singleton
public class BackfillFilendex extends Task {
    private final Datasource datasource;
    private final SearchService searchService;

    @Inject
    public BackfillFilendex(String name, Datasource datasource, SearchService searchService) {
        super("backfill-file-index");
        this.datasource = datasource;
        this.searchService = searchService;
    }

    @Override
    public void execute(Map<String, List<String>> parameters, PrintWriter output) throws Exception {
        List<FileMetadata> allFiles = datasource.listFiles();
        for (FileMetadata file : allFiles) {
            try {
                searchService.readFromDatasourceAndIndex(new FileInfo(file.getBucket(), file.getKey(),
                        Instant.ofEpochMilli(file.getModifiedAt().getTime())));
            } catch (IOException e) {
                // just ignore the file if not able to index it
            }
        }
    }
}
