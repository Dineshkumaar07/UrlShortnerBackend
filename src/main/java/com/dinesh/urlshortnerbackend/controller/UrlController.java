package com.dinesh.urlshortnerbackend.controller;

import com.dinesh.urlshortnerbackend.dto.UrlDTO;
import com.dinesh.urlshortnerbackend.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class UrlController {
    UrlService service;
    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping("/getShorten")
    public ResponseEntity<String> shortenUrl(@Valid @RequestBody UrlDTO urlDTO){
        Optional<String> shortenedUrl = service.getShortenUrl(urlDTO);
        return shortenedUrl.isPresent() ? ResponseEntity.ok(shortenedUrl.get()) : ResponseEntity.notFound().build();
    }
}
