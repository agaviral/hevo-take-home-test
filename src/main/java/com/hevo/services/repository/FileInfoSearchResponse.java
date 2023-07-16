package com.hevo.services.repository;

import com.hevo.services.model.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoSearchResponse {
    private List<FileInfo> fileInfoList;
    private long totalNumberOfResults;
}
