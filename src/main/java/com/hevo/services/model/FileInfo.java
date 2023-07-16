package com.hevo.services.model;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FileInfo {
    private String url;
    private Instant modifiedAt;
}


