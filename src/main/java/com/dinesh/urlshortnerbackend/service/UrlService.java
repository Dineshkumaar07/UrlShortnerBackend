package com.dinesh.urlshortnerbackend.service;

import com.dinesh.urlshortnerbackend.dto.UrlDTO;
import com.dinesh.urlshortnerbackend.model.UrlModel;
import com.dinesh.urlshortnerbackend.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UrlService {

    private final UrlRepository urlRepo;
    public UrlService(UrlRepository urlRepository) {
        this.urlRepo = urlRepository;
    }

    public Optional<String> getShortenUrl(UrlDTO urlDTO) {
        String url = urlDTO.getUrl();
        int ttl = urlDTO.getTtlminutes();
        String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom RANDOM = new SecureRandom();

        List<Character> charList = new ArrayList<>();
        for(char c : CHARACTERS.toCharArray()) {
            charList.add(c);
        }
        Collections.shuffle(charList, RANDOM);

        StringBuilder shortenedUrl = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            shortenedUrl.append(charList.get(i));
        }
        UrlModel urlModel = new UrlModel();
        urlModel.setLongUrl(url);
        urlModel.setShortUrl(shortenedUrl.toString());
        urlModel.setTtlMinutes(ttl);
        urlModel.setCreatedAt(LocalDateTime.now());
        urlRepo.save(urlModel);
        return Optional.of(shortenedUrl.toString());
    }

}
