package com.dinesh.urlshortnerbackend.controller;

import com.dinesh.urlshortnerbackend.dto.UrlDTO;
import com.dinesh.urlshortnerbackend.model.UrlModel;
import com.dinesh.urlshortnerbackend.service.UrlService;
import com.dinesh.urlshortnerbackend.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
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

    @Value("${server.base-url}")
    private String base;

    @PostMapping("/getShorten")
    public ResponseEntity<ApiResponse<String>> shortenUrl(@Valid @RequestBody UrlDTO urlDTO) {
        Optional<String> shortenedUrl = service.getShortenUrl(urlDTO);
        if (shortenedUrl.isPresent()) {

            Optional<String> complete = Optional.of(base + shortenedUrl.get());
            return ResponseEntity.ok(new ApiResponse<>(complete.get(), "Successfully shortened url") );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, "Problem in Generating the Shorten Url"));
    }



    @GetMapping("/getDetails/{shortUrl}")
    public ResponseEntity<ApiResponse<UrlModel>> getDetails(@PathVariable String shortUrl){
        Optional<UrlModel> model = service.getUrl(shortUrl);
        return model.isPresent() && !model.get().isExpired()?
                ResponseEntity.ok(new ApiResponse<>(model.get(), "Success"))
                :ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiResponse<>(null , "Not Found")));
    }


    @GetMapping("/{shorturl}")
    public ResponseEntity<Void> redirectToUrl(@PathVariable String shorturl){
        Optional<UrlModel> url =service.getUrl(shorturl);
        if(url.isPresent() && !url.get().isExpired()){
            String original = url.get().getLongUrl();
            System.out.println("Long URL from DB: " + original);
            String completeUrl = original.startsWith("http://") || original.startsWith("https://")
                    ? original
                    : "https://" + original;
            if (!completeUrl.matches("^https?://.*/$")) { // Ensure ends with '/'
                completeUrl += "/";
            }

            System.out.println("Final Redirect URL: " + completeUrl);

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(completeUrl)).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
