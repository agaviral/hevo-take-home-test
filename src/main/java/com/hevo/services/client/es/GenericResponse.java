package com.hevo.services.client.es;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class GenericResponse<T> {
    private Long primaryTerm;
    private Long sequenceNumber;
    private T document;
}
