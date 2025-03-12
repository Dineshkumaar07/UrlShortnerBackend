package com.dinesh.urlshortnerbackend.repository;

import com.dinesh.urlshortnerbackend.model.UrlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface UrlRepository extends JpaRepository<UrlModel, Long> {
    Optional<UrlModel> findByshortUrl(String url);

    @Query(value = "SELECT * FROM url_mapping WHERE TIMESTAMPADD(MINUTE, ttl_minutes, created_at) < NOW()", nativeQuery = true)
    List<UrlModel> findExpiredUrls();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM url_mapping WHERE TIMESTAMPADD(MINUTE, ttl_minutes, created_at) < NOW()", nativeQuery = true)
    void deleteExpiredUrls();

}
