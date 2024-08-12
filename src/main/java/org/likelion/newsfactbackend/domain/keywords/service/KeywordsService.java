package org.likelion.newsfactbackend.domain.keywords.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface KeywordsService {
    ResponseEntity<?> getKeywords();
}
