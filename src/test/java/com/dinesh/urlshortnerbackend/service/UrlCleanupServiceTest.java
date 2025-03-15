package com.dinesh.urlshortnerbackend.service;

import com.dinesh.urlshortnerbackend.model.UrlModel;
import com.dinesh.urlshortnerbackend.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlCleanupServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlCleanupService urlCleanupService;

    @BeforeEach
    void setUp() {
        urlCleanupService = new UrlCleanupService(urlRepository);
    }

    @Test
    void testCleanUp_WhenExpiredUrlsExist() {

        when(urlRepository.findExpiredUrls()).thenReturn(Collections.nCopies(5, new UrlModel()));

        urlCleanupService.cleanUp();

        verify(urlRepository, times(1)).findExpiredUrls();
        verify(urlRepository, times(1)).deleteExpiredUrls();
    }

    @Test
    void testCleanUp_WhenNoExpiredUrls() {

        when(urlRepository.findExpiredUrls()).thenReturn(Collections.emptyList());

        urlCleanupService.cleanUp();

        verify(urlRepository, times(1)).findExpiredUrls();
        verify(urlRepository, times(1)).deleteExpiredUrls();
    }
}