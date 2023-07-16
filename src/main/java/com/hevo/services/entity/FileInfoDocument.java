package com.hevo.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfoDocument {
    @JsonProperty("url")
    private String url;

    @JsonProperty("string")
    private String content;
}
