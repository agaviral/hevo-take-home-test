package com.hevo.services.task;

import com.hevo.services.client.s3.FileMetadata;
import com.hevo.services.client.s3.S3Client;
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

@Slf4j
@Singleton
public class BackfillFilendex extends Task {
    private final S3Client s3Client;
    private final SearchService searchService;

    @Inject
    public BackfillFilendex(String name, S3Client s3Client, SearchService searchService) {
        super("backfill-file-index");
        this.s3Client = s3Client;
        this.searchService = searchService;
    }

    @Override
    public void execute(Map<String, List<String>> parameters, PrintWriter output) throws Exception {
        List<FileMetadata> allFiles = s3Client.listFiles();
        for (FileMetadata file : allFiles) {
            try {
                searchService.readFromS3AndIndex(new FileInfo(file.getBucket(), file.getKey(),
                        Instant.ofEpochMilli(file.getModifiedAt().getTime())));
            } catch (IOException e) {
                // just ignore the file if not able to index it
            }
        }
    }
}
