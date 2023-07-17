package com.hevo.services.datasource;

import java.io.InputStream;
import java.util.List;

/**
 * Interface which can be implemented by any datasource like S3, Azure Blobstore, HDFS, etc
 */
public interface Datasource {

    /**
     * List all the files in the source along with its metadata
     *
     * @return
     */
    List<FileMetadata> listFiles();

    /**
     * Read the file from the source
     *
     * @param fileName
     * @return
     */
    InputStream readFile(String fileName);

}
