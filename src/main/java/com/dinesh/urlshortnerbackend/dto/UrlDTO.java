package com.dinesh.urlshortnerbackend.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
public class UrlDTO {
    @NotNull(message = "URL cannot be null")
    private String url;

    @NotNull(message = "TTL minutes cannot be null")
    @Min(value = 1, message = "TTL minutes must be at least 1")
    private int ttlminutes;
}
