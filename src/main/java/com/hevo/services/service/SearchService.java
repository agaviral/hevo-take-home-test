package com.hevo.services.service;

import com.hevo.services.resource.response.SearchResponse;

public interface SearchService {
    SearchResponse search(String query);
}
