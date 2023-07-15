package com.hevo.services;

import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class FileSearchServiceConfiguration extends Configuration {
    @NotEmpty
    private final String defaultName = "File Search service";

}
