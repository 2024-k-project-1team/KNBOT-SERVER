package org.likelion.newsfactbackend.domain.news.service;

import org.likelion.newsfactbackend.domain.news.dto.RecommendNewsDto;
import org.likelion.newsfactbackend.domain.news.dto.request.PageRequestNewsDto;
import org.likelion.newsfactbackend.domain.news.dto.request.RequestNewsDto;
import org.likelion.newsfactbackend.domain.news.dto.response.ResponseNewsAnalysisDto;
import org.likelion.newsfactbackend.domain.news.dto.response.ResponseNewsDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public interface NewsService {

    List<ResponseNewsDto> fetchAllNewsArticles(String search) throws IOException;
    List<ResponseNewsDto> fetchNewsArticles(String search) throws IOException;
    Page<ResponseNewsDto> searchNews(PageRequestNewsDto pageRequestNewsDto) throws IOException;
    ResponseNewsDto fetchNewsArticleDetails(String url, String oid) throws IOException;
    ResponseNewsAnalysisDto analyzeNews(String url) throws IOException;

    List<RecommendNewsDto> getRecommendedArticles(List<ResponseNewsDto> allArticles);
    List<ResponseNewsDto> getNewsByCategory(Integer sid, String keyword) throws IOException;

}
