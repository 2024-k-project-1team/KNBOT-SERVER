package org.likelion.newsfactbackend.domain.keywords.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.keywords.dao.KeywordsDAO;
import org.likelion.newsfactbackend.domain.keywords.dto.response.ResponseKeywordsDto;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeywordsDAOImpl implements KeywordsDAO {

    @Override
    public ResponseKeywordsDto getKeywords() {
        return null;
    }
}
