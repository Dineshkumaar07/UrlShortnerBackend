package com.dinesh.urlshortnerbackend.controller;

import com.dinesh.urlshortnerbackend.dto.ApiResponse;
import com.dinesh.urlshortnerbackend.dto.UrlDTO;
import com.dinesh.urlshortnerbackend.model.UrlModel;
import com.dinesh.urlshortnerbackend.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlControllerTest {

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    private UrlDTO urlDTO;
    private UrlModel urlModel;

    @BeforeEach
    void setUp() {
        urlDTO = new UrlDTO("https://example.com", 10);
        urlModel = new UrlModel();
        urlModel.setShortUrl("abc123");
        urlModel.setLongUrl("https://example.com");
        urlModel.setTtlMinutes(10);
        urlModel.setCreatedAt(LocalDateTime.now()); // Setting createdAt to now
    }

    @Test
    void shortenUrl_ShouldReturnShortenedUrl_WhenSuccessful() {
        when(urlService.getShortenUrl(urlDTO)).thenReturn(Optional.of("abc123"));

        ResponseEntity<ApiResponse<String>> response = urlController.shortenUrl(urlDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully shortened url", response.getBody().getMessage());
        assertTrue(response.getBody().getData().contains("abc123"));
    }

    @Test
    void shortenUrl_ShouldReturnNotFound_WhenServiceFails() {
        when(urlService.getShortenUrl(urlDTO)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<String>> response = urlController.shortenUrl(urlDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
    }

    @Test
    void getDetails_ShouldReturnUrlDetails_WhenFound() {
        when(urlService.getUrl("abc123")).thenReturn(Optional.of(urlModel));

        ResponseEntity<ApiResponse<UrlModel>> response = urlController.getDetails("abc123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void getDetails_ShouldReturnNotFound_WhenUrlNotFound() {
        when(urlService.getUrl("abc123")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<UrlModel>> response = urlController.getDetails("abc123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
    }

    @Test
    void getDetails_ShouldReturnNotFound_WhenUrlIsExpired() {
        urlModel.setCreatedAt(LocalDateTime.now().minusMinutes(20)); // Simulate expiration
        when(urlService.getUrl("abc123")).thenReturn(Optional.of(urlModel));

        ResponseEntity<ApiResponse<UrlModel>> response = urlController.getDetails("abc123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
    }

    @Test
    void redirectToUrl_ShouldRedirect_WhenUrlExists() {
        when(urlService.getUrl("abc123")).thenReturn(Optional.of(urlModel));

        ResponseEntity<Void> response = urlController.redirectToUrl("abc123");

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().contains("https://example.com"));
    }

    @Test
    void redirectToUrl_ShouldReturnNotFound_WhenUrlNotExists() {
        when(urlService.getUrl("abc123")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = urlController.redirectToUrl("abc123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void redirectToUrl_ShouldReturnNotFound_WhenUrlIsExpired() {
        urlModel.setCreatedAt(LocalDateTime.now().minusMinutes(20)); // Simulate expiration
        when(urlService.getUrl("abc123")).thenReturn(Optional.of(urlModel));

        ResponseEntity<Void> response = urlController.redirectToUrl("abc123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
