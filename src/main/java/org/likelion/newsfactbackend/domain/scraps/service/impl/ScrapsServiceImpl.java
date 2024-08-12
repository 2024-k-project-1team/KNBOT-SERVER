package org.likelion.newsfactbackend.domain.scraps.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.scraps.dao.ScrapsDAO;
import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestSaveScrapsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestScrapsNewsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.response.ResponseScrapsNewsDto;
import org.likelion.newsfactbackend.domain.scraps.service.ScrapsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapsServiceImpl implements ScrapsService {
    private final ScrapsDAO scrapsDAO;

    @Override
    public ResponseScrapsNewsDto getScraps(RequestScrapsNewsDto requestScrapsNewsDto) {
        return null;
    }

    @Override
    public Boolean findScrapsById(Long id) {
        return null;
    }

    @Override
    public Boolean scrapNews(RequestSaveScrapsDto requestSaveScrapsDto, String nickname) {
        return scrapsDAO.saveNews(requestSaveScrapsDto, nickname);
    }

    @Override
    public Boolean deleteScrap(Long id, String nickName) {
        log.info("[service] id : {}", id);
        return scrapsDAO.deleteNews(id,nickName);
    }
    @Override
    public Page<ResponseScrapsNewsDto> getScrapsByPage(Pageable pageable, String nickName){
        return scrapsDAO.getMyNews(pageable, nickName);
    }
}
