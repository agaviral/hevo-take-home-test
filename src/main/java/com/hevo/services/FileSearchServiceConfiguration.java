package com.hevo.services;

import com.hevo.services.client.es.ElasticSearchConfig;
import com.hevo.services.client.s3.S3Buckets;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FileSearchServiceConfiguration extends Configuration {
    @NotEmpty
    private final String defaultName = "File Search service";

    @Valid
    @NotNull
    private ElasticSearchConfig elasticSearchDb;

    @Valid
    @NotNull
    private S3Buckets s3Buckets;
}
