package com.hevo.services.task;

import com.hevo.services.client.s3.FileMetadata;
import com.hevo.services.client.s3.S3Client;
import com.hevo.services.model.FileData;
import com.hevo.services.parser.FileParser;
import com.hevo.services.parser.FileParserSelector;
import com.hevo.services.parser.ParserNotFoundException;
import com.hevo.services.repository.FileRepository;
import io.dropwizard.servlets.tasks.Task;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Singleton
public class BackfillFileIndex extends Task {
    private final FileRepository fileRepository;
    private final S3Client s3Client;
    private final FileParserSelector fileParserSelector;

    @Inject
    public BackfillFileIndex(String name, FileRepository fileRepository, S3Client s3Client,
                             FileParserSelector fileParserSelector) {
        super("backfill-file-index");
        this.fileRepository = fileRepository;
        this.s3Client = s3Client;
        this.fileParserSelector = fileParserSelector;
    }

    @Override
    public void execute(Map<String, List<String>> parameters, PrintWriter output) throws Exception {
        List<FileMetadata> allFiles = s3Client.listFiles();
        for (FileMetadata file : allFiles) {
            String content = "";
            try (InputStream fileData = s3Client.readFile(file.getKey())) {
                FileParser parser = fileParserSelector.getFileParser(file.getKey());
                content = parser.getContent(fileData);
            } catch (IOException | ParserNotFoundException e) {
                // just ignore the file if
                // - no parser was found for it
                // - it cannot be read from AWS
                // - parser throws an exception parsing it
                log.error("Error parsing file: " + file.getUrl());
                continue;
            }

            fileRepository.upsertFile(
                    FileData.builder()
                            .url(file.getUrl())
                            .modifiedAt(Instant.ofEpochMilli(file.getModifiedAt().getTime()))
                            .content(content)
                            .build()
            );
        }
    }
}
