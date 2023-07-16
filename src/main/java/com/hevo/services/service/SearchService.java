package com.hevo.services.service;

import com.hevo.services.model.FileInfo;
import com.hevo.services.resource.response.SearchResponse;

import java.io.IOException;

public interface SearchService {
    SearchResponse search(String query);

    void readFromS3AndIndex(FileInfo fileInfo) throws IOException;

    void removeFromIndex(FileInfo fileInfo);
}
