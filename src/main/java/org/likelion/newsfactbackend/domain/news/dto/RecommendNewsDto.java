package org.likelion.newsfactbackend.domain.news.dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder

public class RecommendNewsDto {
    private String companyLogo;
    private String title;
    private String thumbnailUrl;
    private String newsUrl;
}