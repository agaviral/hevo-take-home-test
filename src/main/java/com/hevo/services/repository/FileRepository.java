package com.hevo.services.repository;

import com.hevo.services.model.FileData;

import java.io.IOException;

public interface FileRepository {

    FileInfoSearchResponse searchFile(String query, int limit, int offset) throws IOException;

    void upsertFile(FileData fileData) throws IOException;

    void deleteFile(String url) throws IOException;
}
