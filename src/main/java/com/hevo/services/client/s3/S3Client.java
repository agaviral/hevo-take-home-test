package com.hevo.services.client.s3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.hevo.services.FileSearchServiceConfiguration;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class S3Client {
    private final AmazonS3 s3client;
    private final String fileBucket;

    @Inject
    public S3Client(FileSearchServiceConfiguration config) {
        s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
        fileBucket = config.getS3Buckets().getFileBucket();
    }

    public List<String> listFiles() {
        ObjectListing listing = s3client.listObjects(fileBucket);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3client.listNextBatchOfObjects (listing);
            summaries.addAll (listing.getObjectSummaries());
        }

        return summaries.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    public byte[] readFile(String fileName) throws IOException {
        S3Object object = s3client.getObject(fileBucket, fileName);
        return IOUtils.toByteArray(object.getObjectContent());
    }
}
