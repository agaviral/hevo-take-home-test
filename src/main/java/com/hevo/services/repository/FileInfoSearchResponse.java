package com.hevo.services.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoSearchResponse {
    private List<FileInfo> fileInfoList;
    private long totalNumberOfResults;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private String url;
        private Instant modifiedAt;
    }
}
