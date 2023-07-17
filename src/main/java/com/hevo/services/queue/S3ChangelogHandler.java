package com.hevo.services.queue;

import com.hevo.services.model.FileInfo;
import com.hevo.services.service.SearchService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

/**
 * A simple handler which inserts or deletes file from the index given a S3 message.
 */
@Slf4j
public class S3ChangelogHandler implements Handler<S3ChangelogEvent> {
    private static final String S3EVENT_CREATE_PREFIX = "ObjectCreated";
    private static final String S3EVENT_REMOVE_PREFIX = "ObjectRemoved";

    private final SearchService searchService;

    @Inject
    public S3ChangelogHandler(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void handle(S3ChangelogEvent message) throws Exception {
        for (S3ChangelogEvent.Record record : message.getRecords()) {
            if (record.getEventName().startsWith(S3EVENT_CREATE_PREFIX)) {
                FileInfo fileInfo = createFileMetadata(record);
                searchService.readFromDatasourceAndIndex(fileInfo);
            }

            if (record.getEventName().startsWith(S3EVENT_REMOVE_PREFIX)) {
                FileInfo fileInfo = createFileMetadata(record);
                searchService.removeFromIndex(fileInfo);
            }
        }
    }

    private FileInfo createFileMetadata(S3ChangelogEvent.Record record) {
        return new FileInfo(record.getS3().getBucket().getName(), record.getS3().getObject().getKey(),
                record.getEventTime());
    }

}
