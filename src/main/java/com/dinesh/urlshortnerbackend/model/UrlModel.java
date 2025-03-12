package com.dinesh.urlshortnerbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "url_mapping")
public class UrlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime createdAt;
    @Column(nullable = false)
    private int ttlMinutes;

    public UrlModel() {
        this.createdAt = LocalDateTime.now();

    }

    @Column(nullable = false, length = 500)
    private String longUrl;

    @Column(nullable = false)
    private String shortUrl;


    public LocalDateTime getExpiryTime() {
        return createdAt.plusMinutes(ttlMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(getExpiryTime());
    }

}
