package com.hevo.services.resource.response;

import com.hevo.services.model.FileInfo;
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
}
