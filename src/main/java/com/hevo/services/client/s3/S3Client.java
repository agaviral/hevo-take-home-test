package com.hevo.services.client.s3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.hevo.services.FileSearchServiceConfiguration;
import org.eclipse.jetty.util.URIUtil;

import javax.inject.Inject;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
                        .url(constructUrl(fileBucket, s.getKey()))
                        .modifiedAt(s.getLastModified())
                        .build())
                .collect(Collectors.toList());
    }

    public InputStream readFile(String fileName) {
        S3Object object = s3client.getObject(fileBucket, fileName);
        return object.getObjectContent();
    }

    private String constructUrl(String fileBucket, String key) {
        return String.format("https://%s.s3.ap-south-1.amazonaws.com/%s",
                fileBucket, URIUtil.encodePath(key));
    }
}
