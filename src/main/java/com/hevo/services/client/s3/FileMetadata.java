package com.hevo.services.client.s3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {
    private String key;
    private String bucket;
    private Date modifiedAt;
}
