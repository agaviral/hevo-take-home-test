package com.hevo.services.task;

import com.hevo.services.repository.FileRepository;
import com.hevo.services.service.SearchService;
import io.dropwizard.servlets.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Singleton
public class BackfillElasticSearch extends Task {
    private final FileRepository fileRepository;

    @Inject
    public BackfillElasticSearch(String name, FileRepository fileRepository) {
        super("backfill-es");
        this.fileRepository = fileRepository;
    }

    @Override
    public void execute(Map<String, List<String>> parameters, PrintWriter output) throws Exception {

    }
}
