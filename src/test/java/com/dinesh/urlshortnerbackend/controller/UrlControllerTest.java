package com.dinesh.urlshortnerbackend.controller;

import com.dinesh.urlshortnerbackend.dto.UrlDTO;
import com.dinesh.urlshortnerbackend.model.UrlModel;
import com.dinesh.urlshortnerbackend.service.UrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlControllerUnitTest {

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    @Test
    void testShortenUrl_Success() {

        UrlDTO urlDTO = new UrlDTO("example.com", 10);
        String shortUrl = "abc123";

        when(urlService.getShortenUrl(urlDTO)).thenReturn(Optional.of(shortUrl));

        ResponseEntity<String> response = urlController.shortenUrl(urlDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(shortUrl, response.getBody());
    }

    @Test
    void testRedirectToUrl_Success() {

        String shortUrl = "abc123";
        UrlModel urlModel = new UrlModel();
        urlModel.setShortUrl(shortUrl);
        urlModel.setLongUrl("example.com");

        when(urlService.getUrl(shortUrl)).thenReturn(Optional.of(urlModel));

        ResponseEntity<Void> response = urlController.redirectToUrl(shortUrl);
                    
        assertEquals(302, response.getStatusCodeValue());
        assertEquals("https://example.com", response.getHeaders().getLocation().toString());
    }
}
