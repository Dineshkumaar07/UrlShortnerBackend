package com.dinesh.urlshortnerbackend.controller;

import com.dinesh.urlshortnerbackend.dto.UrlDTO;
import com.dinesh.urlshortnerbackend.model.UrlModel;
import com.dinesh.urlshortnerbackend.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping("/getDetails/{shortUrl}")
    public Optional<UrlModel> getDetails(@PathVariable String shortUrl){
        Optional<UrlModel> model = service.getUrl(shortUrl);
        return model;
    }


    @GetMapping("/{shorturl}")
    public ResponseEntity<Void> redirectToUrl(@PathVariable String shorturl){
        Optional<UrlModel> url =service.getUrl(shorturl);
        if(url.isPresent()){
            String original = url.map(UrlModel::getLongUrl).orElse("");
            String completeUrl = "https://" + original;
            System.out.println(completeUrl);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(completeUrl)).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://www.google.com")).build();
    }
}
