package com.hevo.services.queue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class S3ChangelogEvent {
    @JsonProperty("Records")
    private List<Record> records;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Record {
        private String eventName;
        private Instant eventTime;
        private S3 s3;

        @Getter
        @Setter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class S3 {
            private Bucket bucket;
            private S3Obj object;

            @Getter
            @Setter
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Bucket {
                private String name;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class S3Obj {
                private String key;
            }
        }
    }


}
