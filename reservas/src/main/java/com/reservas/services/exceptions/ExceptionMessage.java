package com.reservas.services.exceptions;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExceptionMessage {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;
    private List<String> mensages;
}
