package com.hevo.services.repository;

import com.hevo.services.model.FileData;
import com.hevo.services.model.FileInfo;

import java.util.List;

public interface FileRepository {

    List<FileInfo> searchFile(String query);

    void upsertFile(FileData fileData);
}
