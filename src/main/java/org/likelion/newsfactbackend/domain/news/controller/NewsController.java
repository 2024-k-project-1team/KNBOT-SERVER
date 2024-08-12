package org.likelion.newsfactbackend.domain.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.news.dto.RecommendNewsDto;
import org.likelion.newsfactbackend.domain.news.dto.request.PageRequestNewsDto;
import org.likelion.newsfactbackend.domain.news.dto.response.ResponseNewsAnalysisDto;
import org.likelion.newsfactbackend.domain.news.dto.response.ResponseNewsDto;
import org.likelion.newsfactbackend.domain.news.exception.ErrorCode;
import org.likelion.newsfactbackend.domain.news.exception.NewsException;
import org.likelion.newsfactbackend.domain.news.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor

public class NewsController {

    private final NewsService newsService;
    @Operation(summary = "키워드 입력 시, 관련 뉴스 기사를 반환합니다.")
    @GetMapping("/search") //http://localhost:8080/api/v1/news/search?keyword=%ED%8A%B8%EB%9F%BC%ED%94%84&size=10
    public ResponseEntity<Page<ResponseNewsDto>> searchNews(@RequestParam(defaultValue = "4") Long size, @RequestParam String keyword) throws IOException {
        PageRequestNewsDto newsRequestDto = new PageRequestNewsDto(size, keyword);

        if (isNull(newsRequestDto.getKeyword())) {
            throw new NewsException(ErrorCode.EMPTY_KEYWORD);
        }
        if (newsRequestDto.isOverSize(size)) {
            throw new NewsException(ErrorCode.EMPTY_SIZE);
        }

        try {
            Page<ResponseNewsDto> responseNewsDtoPage = newsService.searchNews(newsRequestDto);
            List<ResponseNewsDto> content = responseNewsDtoPage.getContent();

            for (ResponseNewsDto news : content) {
                if (news.isTitleEmpty()) {
                    news.titleDefault(keyword);
                    log.warn("{} title 값이 없습니다.", news);
                }
                news.validateFields();
            }
            return ResponseEntity.status(HttpStatus.OK).body(responseNewsDtoPage);
        } catch (IOException e) {
            log.error("서버 오류: {}", e.getMessage());
            throw new NewsException(ErrorCode.SERVER_ERROR);
        }
    }

    /*https://newspect.co.kr/api/v1/news-details?url=${url}*/

    @Operation(summary = "뉴스 상세페이지를 반환합니다.")
    @GetMapping("/news-details")
    public ResponseEntity<?> getNews(@RequestParam String url, @RequestParam String oid) throws IOException {

        if (isNull(oid)) {
            throw new NewsException(ErrorCode.NOT_FOUND_OID);
        }
        if (isNull(url)) {
            throw new NewsException(ErrorCode.NOT_FOUND_NEWS_URL);
        }
        ResponseNewsDto responseNewsDto = newsService.fetchNewsArticleDetails(url, oid);
        return ResponseEntity.status(HttpStatus.OK).body(responseNewsDto);
    }

    @Operation(summary = "뉴스 감정 분석 + 워드 클라우드", description = "기사의 감정 분석 결과와 워드클라우드 결과를 반환합니다.")
    @GetMapping("/news-analysis")
    public ResponseEntity<?> getNewsAnalysis(@RequestParam String url) throws IOException{
        if (isNull(url)) {
            throw new NewsException(ErrorCode.NOT_FOUND_NEWS_URL);
        }
        return ResponseEntity.ok(newsService.analyzeNews(url));
    }


    private boolean isNull(String str) {
        return str == null || str.trim().isEmpty();
    }

    @Operation(summary = "추천 뉴스 기사를 반환합니다.")
    @GetMapping("/search/recommend")
    public List<RecommendNewsDto> recommendNews(@RequestParam String keyword) throws IOException {
        List<ResponseNewsDto> allArticles = newsService.fetchAllNewsArticles(keyword);
        if (isNull(keyword)) {
            throw new NewsException(ErrorCode.EMPTY_KEYWORD);
        }
        return newsService.getRecommendedArticles(allArticles);

    }

    @Operation(summary = "카테고리별 뉴스 기사를 반환합니다.")
    @GetMapping("/search/category")
    public ResponseEntity getNewsByCategory(@RequestParam Integer sid, @RequestParam String search) throws IOException {

        if (sid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당하는 카테고리가 없습니다.");
        }
        List<ResponseNewsDto> newsByCategory = newsService.getNewsByCategory(sid, search);

        if (newsByCategory == null || newsByCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 카테고리의 뉴스 기사가 없습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(newsByCategory);

    }
}

