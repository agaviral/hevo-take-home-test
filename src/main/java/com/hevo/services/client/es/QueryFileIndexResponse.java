package com.hevo.services.client.es;

import com.hevo.services.entity.FileInfoDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryFileIndexResponse {
    private List<FileInfoDocument> files;
    private long totalNumberOfResults;
}
