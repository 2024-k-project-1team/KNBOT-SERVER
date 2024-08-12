package org.likelion.newsfactbackend.domain.keywords.dao;

import org.likelion.newsfactbackend.domain.keywords.dto.response.ResponseKeywordsDto;
import org.springframework.http.ResponseEntity;

public interface KeywordsDAO {
    ResponseKeywordsDto getKeywords();
}
