package com.hevo.services.datasource;

import java.io.InputStream;
import java.util.List;

public interface Datasource {

    public List<FileMetadata> listFiles();

    InputStream readFile(String fileName);

}
