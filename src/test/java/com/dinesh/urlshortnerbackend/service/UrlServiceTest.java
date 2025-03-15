package com.dinesh.urlshortnerbackend.service;

import com.dinesh.urlshortnerbackend.dto.UrlDTO;
import com.dinesh.urlshortnerbackend.model.UrlModel;
import com.dinesh.urlshortnerbackend.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @BeforeEach
    void setUp() {
        urlService = new UrlService(urlRepository);
    }

    @Test
    void testGetShortenUrl_Success() {
        UrlDTO urlDTO = new UrlDTO("https://example.com", 10);

        Optional<String> shortUrl = urlService.getShortenUrl(urlDTO);

        assertTrue(shortUrl.isPresent());
        assertEquals(6, shortUrl.get().length());
        verify(urlRepository, times(1)).save(any(UrlModel.class));
    }

    @Test
    void testGetUrl_Success() {
        String shortUrl = "abc123";
        UrlModel urlModel = new UrlModel();
        urlModel.setShortUrl(shortUrl);
        urlModel.setLongUrl("https://example.com");
        urlModel.setCreatedAt(LocalDateTime.now());
        urlModel.setTtlMinutes(10);

        when(urlRepository.findByshortUrl(shortUrl)).thenReturn(Optional.of(urlModel));

        Optional<UrlModel> retrievedUrl = urlService.getUrl(shortUrl);

        assertTrue(retrievedUrl.isPresent());
        assertEquals("https://example.com", retrievedUrl.get().getLongUrl());
        verify(urlRepository, times(1)).findByshortUrl(shortUrl);
    }

    @Test
    void testGetUrl_NotFound() {

        String shortUrl = "xyz789";
        when(urlRepository.findByshortUrl(shortUrl)).thenReturn(Optional.empty());

        Optional<UrlModel> retrievedUrl = urlService.getUrl(shortUrl);

        assertFalse(retrievedUrl.isPresent());
        verify(urlRepository, times(1)).findByshortUrl(shortUrl);
    }


}
