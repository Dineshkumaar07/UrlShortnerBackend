package com.dinesh.urlshortnerbackend.service;

import com.dinesh.urlshortnerbackend.repository.UrlRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UrlCleanupService {
    private final UrlRepository urlRepository;

    public UrlCleanupService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void cleanUp() {
        int count = urlRepository.findExpiredUrls().size();
        urlRepository.deleteExpiredUrls();
        System.out.println(count + " expired urls have been cleaned up");

    }
}
