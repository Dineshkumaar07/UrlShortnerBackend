package com.dinesh.urlshortnerbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "url_mapping")
public class UrlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime createdAt;

    public UrlModel() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(nullable = false, length = 500)
    private String longUrl;

    @Column(nullable = false)
    private String shortUrl;

    @Column(nullable = false)
    private int ttlMinutes;

    public LocalDateTime getExpiryTime() {
        return createdAt.plusMinutes(ttlMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(getExpiryTime());
    }

}
