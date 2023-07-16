package com.hevo.services.client.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hevo.services.FileSearchServiceConfiguration;
import com.hevo.services.entity.FileInfoDocument;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ElasticSearchClient {
    private static final String FILE_CONTENT_COLUMN_NAME = "content";

    private final ElasticsearchClient esClient;
    private final String fileInfoIndex;

    @Inject
    public ElasticSearchClient(FileSearchServiceConfiguration configuration, ObjectMapper objectMapper){
        RestClient restClient = RestClient
                .builder(HttpHost.create(configuration.getElasticSearchDb().getHostname()))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper(objectMapper));

        esClient = new ElasticsearchClient(transport);
        fileInfoIndex = configuration.getElasticSearchDb().getFileInfoIndex();
    }

    /**
     * Queries the file info index and return the list of file info documents found.
     * Also returns the total number of file info document matching the query
     * @param query the term which is being search for in the file
     * @param limit the number of file documents which should be returned
     * @param offset the offset from which the documents should be read
     * @return all the files which contain the given query together with total files indexed
     * @throws IOException in case of an error communicating with ES
     */
    public QueryFileIndexResponse queryFileInfo(String query, int limit, int offset) throws IOException {
        SearchResponse<FileInfoDocument> response = esClient.search(s -> s
                        .index(fileInfoIndex)
                        .query(q -> q.multiMatch(m -> m.query(query)))
                        .size(limit)
                        .from(offset),
                FileInfoDocument.class
        );

        long total = response.hits().total().value();
        List<FileInfoDocument> result = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        return QueryFileIndexResponse.builder()
                .files(result)
                .totalNumberOfResults(total)
                .build();
    }


    public Optional<GenericResponse<FileInfoDocument>> getFileInfo(String url) throws IOException {
        BooleanResponse exists =
                esClient.exists(r -> r.index(fileInfoIndex).id(url));
        if (!exists.value()) {
            return Optional.empty();
        }

        GetResponse<FileInfoDocument> response =
                esClient.get(
                        request -> request.index(fileInfoIndex).id(url),
                        FileInfoDocument.class);

        return Optional.of(
                GenericResponse.<FileInfoDocument>builder()
                        .primaryTerm(response.primaryTerm())
                        .sequenceNumber(response.seqNo())
                        .document(response.source())
                        .build());
    }

    /**
     * Inserts the given file info document to ES.
     *
     * @param fileInfoDocument the document we want to insert
     * @return true if the document was successfully inserted, false otherwise
     * @throws IOException in case of error communicating with SD
     */
    public boolean insertFileInfo(FileInfoDocument fileInfoDocument) throws IOException {
        IndexResponse response =
                esClient.index(r -> r.index(fileInfoIndex).id(fileInfoDocument.getUrl()).document(fileInfoDocument));
        return response.shards().failed().intValue() == 0;
    }

    /**
     * Update the file info document given a primary term and sequence number.
     * This ensures optimistic locking.
     *
     * @param document the document we want to update
     * @param primaryTerm the primary term we got when querying for it
     * @param sequenceNumber the sequence number we got when querying for it
     * @return true if the document was update, false otherwise
     * @throws IOException in case of an error when communicating with ES
     */
    public boolean updateFileInfo(FileInfoDocument document, long primaryTerm, long sequenceNumber) throws IOException {
        UpdateResponse<FileInfoDocument> response =
                esClient.update(
                        r ->
                                r.index(fileInfoIndex)
                                        .id(document.getUrl())
                                        .doc(document)
                                        .ifPrimaryTerm(primaryTerm)
                                        .ifSeqNo(sequenceNumber),
                        FileInfoDocument.class);

        return response.shards().failed().intValue() == 0;
    }
}
