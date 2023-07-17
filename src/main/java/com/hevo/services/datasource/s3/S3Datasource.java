package com.hevo.services.datasource.s3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.hevo.services.FileSearchServiceConfiguration;
import com.hevo.services.datasource.Datasource;
import com.hevo.services.datasource.FileMetadata;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class S3Datasource implements Datasource {
    private final AmazonS3 s3client;
    private final String fileBucket;

    @Inject
    public S3Datasource(FileSearchServiceConfiguration config) {
        s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
        fileBucket = config.getS3Buckets().getFileBucket();
    }

    public List<FileMetadata> listFiles() {
        ObjectListing listing = s3client.listObjects(fileBucket);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3client.listNextBatchOfObjects(listing);
            summaries.addAll(listing.getObjectSummaries());
        }

        return summaries.stream()
                .map(s -> FileMetadata.builder()
                        .key(s.getKey())
                        .bucket(fileBucket)
                        .modifiedAt(s.getLastModified())
                        .build())
                .collect(Collectors.toList());
    }

    public InputStream readFile(String fileName) {
        S3Object object = s3client.getObject(fileBucket, fileName);
        return object.getObjectContent();
    }
}
