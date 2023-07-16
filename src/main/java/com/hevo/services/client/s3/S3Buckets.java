package com.hevo.services.client.s3;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class S3Buckets {
    @NotNull
    @NotEmpty
    private String fileBucket;


}
