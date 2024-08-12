/*
package org.likelion.newsfactbackend.domain.news.dto.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ResponseNewsDtoTest {

    @Test
    void testValidateFieldsWithEmptyCompany() {
        // Mock 객체 생성
        ResponseNewsDto newsDto = mock(ResponseNewsDto.class);

        // Mock 객체의 getTitle() 메서드가 호출될 때 "테스트 기사 제목"을 리턴하도록 설정
        when(newsDto.getTitle()).thenReturn("테스트 기사 제목");

        // Mock 객체의 getCompany() 메서드가 호출될 때 "언론사"를 리턴하도록 설정
        when(newsDto.getCompany()).thenReturn("언론사");

        // 테스트할 메서드 호출 시 Mock 객체의 동작 설정
        newsDto.setTitle("테스트 기사 제목");
        newsDto.setSubTitle("테스트 기사 내용");
        newsDto.setThumbnail("테스트 썸네일");
        newsDto.setPublishDate("2024-07-20");
        newsDto.setNewsUrl("https://example.com/news");

        // 빈 언론사 값 설정
        when(newsDto.getCompany()).thenReturn("");

        // verify()를 사용하여 Mock 객체의 메서드 호출 여부를 검증할 수도 있습니다.
        verify(newsDto).getCompany();
    }

}*/
