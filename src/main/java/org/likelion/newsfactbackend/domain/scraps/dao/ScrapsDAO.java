package org.likelion.newsfactbackend.domain.scraps.dao;

import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestSaveScrapsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestScrapsNewsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.response.ResponseScrapsNewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ScrapsDAO {
    boolean saveNews(RequestSaveScrapsDto requestSaveScrapsDto, String nickname);
    Page<ResponseScrapsNewsDto> getMyNews(Pageable pageable, String nickname);
    boolean deleteNews(Long id, String nickName);
}
