package org.likelion.newsfactbackend.domain.scraps.dto.response;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseScrapsNewsDto {
    private Long id;
    private String company;
    private String title;
    private String date;
    private String thumbNailUrl;
    private String newsUrl;
    private String companyLogo;
    private String category;
}
