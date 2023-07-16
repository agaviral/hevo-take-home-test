package com.hevo.services.repository;

import com.hevo.services.model.FileData;

public interface FileRepository {

    FileInfoSearchResponse searchFile(String query, int limit, int offset);

    void upsertFile(FileData fileData);
}
