package org.likelion.newsfactbackend.domain.news.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.news.exception.ErrorCode;
import org.likelion.newsfactbackend.domain.news.exception.NewsException;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@SuperBuilder
public class ResponseNewsDto {

    @Builder.Default
    private String companyLogo = "/images/default_logo.png"; /* Default 로고 이미지 경로 */
    private String company = "미상";
    private List<String> imgUrl = null;
    private List<String> imgContentList = null;
    private String title;
    private String subTitle = "내용 없음";
    private String publishDate = LocalDate.now().toString(); /* Default 현재 시간 */
    private String newsUrl; // URL
    private List<String> contentsList; // 줄바꿈 뉴스 리스트
    private String category;
    private String thumbnailUrl = "/images/default_thumbnail.jpg";
    private String contents;
    private List<String> cleanUpList;

    public ResponseNewsDto(String companyLogo, String company, List<String> imgUrl, String title, String subTitle, String thumbnailUrl, String publishDate, String newsUrl) {
        this.companyLogo = companyLogo;
        this.company = company;
        this.imgUrl = imgUrl;
        this.title = title;
        this.subTitle = subTitle;
        this.thumbnailUrl = thumbnailUrl;
        this.publishDate = publishDate;
        this.newsUrl = newsUrl;
    }

    /* isTitleEmpty = title 값이 비었는 지 확인 */
    public boolean isTitleEmpty() {
        return title.isEmpty();
    }

    /* titleDefault = title 값이 비었을 시, "keyword 관련 기사입니다." 값을 저장  */
    public void titleDefault(String keyword) {
        title = keyword + "관련 기사입니다.";
    }

    /* validateFields = NewsUrl이 비었을 시 -> 에러*/
    public void validateFields() {
        if (newsUrl.isEmpty()) {
            log.warn("뉴스 링크 값이 존재하지않습니다.");
            throw new NewsException(ErrorCode.NOT_FOUND_NEWS_URL);
        }
    }
}