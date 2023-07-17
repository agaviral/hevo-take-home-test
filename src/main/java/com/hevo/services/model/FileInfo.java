package com.hevo.services.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jetty.util.URIUtil;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class FileInfo {
    private String url;
    private String key;
    private Instant modifiedAt;

    public FileInfo(String s3Bucket, String key, Instant modifiedAt) {
        this.url = String.format("https://%s.s3.ap-south-1.amazonaws.com/%s",
                s3Bucket, URIUtil.encodePath(key));
        this.key = key;
        this.modifiedAt = modifiedAt;
    }
}


