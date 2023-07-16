package com.hevo.services.resource;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hevo.services.resource.response.SearchResponse;
import com.hevo.services.service.SearchService;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@NoArgsConstructor
@Singleton
public class SearchResource {
    @Inject
    SearchService searchService;

    @GET
    @Path("/search")
    public SearchResponse searchFile(
            @QueryParam("q") @NotEmpty String query,
            @QueryParam("limit") @DefaultValue("10")  @Min(1) @Max(10) int limit,
            @DefaultValue("0") @QueryParam("offset") @Min(0) int offset) throws JsonProcessingException {
        return searchService.search(query);
    }
}

/**

 curl -X PUT "localhost:9200/file-info?pretty" -H 'Content-Type: application/json' -d'
 {
     "mappings": {
         "properties": {
             "url": {
                 "type": "text"
             },
             "content": {
                 "type": "text",
                 "store": false
             }
         }
     }
 }
 '



 */