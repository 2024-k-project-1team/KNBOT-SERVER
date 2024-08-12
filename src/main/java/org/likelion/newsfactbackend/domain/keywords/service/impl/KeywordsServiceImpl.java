package org.likelion.newsfactbackend.domain.keywords.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.keywords.service.KeywordsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeywordsServiceImpl implements KeywordsService {
    @Override
    public ResponseEntity<?> getKeywords() {
        return null;
    }
}
