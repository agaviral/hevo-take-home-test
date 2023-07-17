package com.hevo.services.resource.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SearchResponse {
    private int numResults;
    private long totalNumberOfResults;
    private List<FileInfo> files;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private String url;
        private String modifiedAt;
    }
}
