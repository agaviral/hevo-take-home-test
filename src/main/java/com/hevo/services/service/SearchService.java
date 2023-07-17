package com.hevo.services.service;

import com.hevo.services.model.FileInfo;
import com.hevo.services.resource.response.SearchResponse;

import java.io.IOException;

/**
 * An interface which helps search for files and perform CRUD on the index
 */
public interface SearchService {
    /**
     * Searches the underlying repository for the files containing the given query.
     * The limit and offset parameters can be used for pagination.
     *
     * @param query  the term which the underlying files must contain
     * @param limit  the total number of files to fetch
     * @param offset the offset, from which to start reading.
     * @return the list of files found as well the total number of files that contain this term
     */
    SearchResponse search(String query, int limit, int offset);

    /**
     * Reads the given file from the underlying cloud storage and indexes it.
     *
     * @param fileInfo the file which to read and index
     * @throws IOException in case unable to read file, find the parser for it or parse it
     */
    void readFromDatasourceAndIndex(FileInfo fileInfo) throws IOException;

    /**
     * Removes the given file from the index.
     * This can be called in case the underlying cloud storage has a file deleted
     *
     * @param fileInfo the file which needs to be deleted
     */
    void removeFromIndex(FileInfo fileInfo);
}
