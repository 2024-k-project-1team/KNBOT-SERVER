package org.likelion.newsfactbackend.domain.scraps.service;

import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestSaveScrapsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestScrapsNewsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.response.ResponseScrapsNewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ScrapsService {
    /*
     * desc: scrap 목록 전체를 가지고 오는 서비스
     * @Param: RequestScrapsNewsDto
     * @Return: ResponseScrapsNewsDto
    */
    ResponseScrapsNewsDto getScraps(RequestScrapsNewsDto requestScrapsNewsDto);
    /*
     * desc: scrap id를 입력받아 존재 여부를 판단하는 서비스
     * @Param: id -> scrap id
     * @Return: Boolean -> exist = true, not exist -> false
    */
    Boolean findScrapsById(Long id);
    /*
     * desc: scrap을 저장하는 서비스
     * @Param: RequestSaveScrapsDto
     * @Return: Boolean -> 정상 저장 = true, 저장 시 오류 발생 -> false
    */
    Boolean scrapNews(RequestSaveScrapsDto requestSaveScrapsDto, String nickName);
    /*
     * desc: scrap을 삭제하는 서비스
     * @Param: Long -> 삭제하려는 scrap의 id
     * @Return: Boolean -> 정상 삭제 = true, 삭제 시 오류 발생 -> false
    */
    Boolean deleteScrap(Long id, String nickName);
    /*
     * desc: scrap 조회 서비스
     * @Param: Pageable -> page 조회 쿼리 정보
     * @Return: Page -> 조회된 scrap 페이지 정보
     */
    Page<ResponseScrapsNewsDto> getScrapsByPage(Pageable pageable, String nickname);
}




