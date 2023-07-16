package com.hevo.services.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FileInfo {
    private String fileName;
    private String url;
}


