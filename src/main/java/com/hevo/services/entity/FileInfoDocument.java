package com.hevo.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hevo.services.model.FileData;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

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

    @JsonProperty("modified_at")
    private Instant modifiedAt;

    public static FileInfoDocument from(FileData fileData) {
            return FileInfoDocument.builder()
                    .url(fileData.getUrl())
                    .content(fileData.getContent())
                    .modifiedAt(fileData.getModifiedAt())
                    .build();
    }
}
