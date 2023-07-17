package com.hevo.services.repository;

import com.hevo.services.model.FileData;

import java.io.IOException;

/**
 * An interface to repository which hold the file index.
 * Support search and basic CRUD.
 */
public interface FileRepository {

    /**
     * Searches for files and return a list which match the given query.
     * limit and offset can be use to paginate the results
     *
     * @param query
     * @param limit
     * @param offset
     * @return
     * @throws IOException
     */
    FileInfoSearchResponse searchFile(String query, int limit, int offset) throws IOException;

    /**
     * Upserts the given file in the index, ie, creates if not present otherwise updates it.
     * The id is the url given.
     *
     * @param fileData
     * @throws IOException
     */
    void upsertFile(FileData fileData) throws IOException;

    /**
     * Deletes the given file from the index
     *
     * @param url
     * @throws IOException
     */
    void deleteFile(String url) throws IOException;
}
