package com.hevo.services.service;

import com.hevo.services.repository.FileRepository;
import com.hevo.services.resource.response.SearchResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

public interface SearchService {
    SearchResponse search(String query);
}
