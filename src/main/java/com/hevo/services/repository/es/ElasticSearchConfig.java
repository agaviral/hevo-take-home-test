package com.hevo.services.repository.es;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ElasticSearchConfig {
    @NotNull
    @NotEmpty
    private String hostname;

    @NotNull
    @NotEmpty
    private String fileInfoIndex;
}