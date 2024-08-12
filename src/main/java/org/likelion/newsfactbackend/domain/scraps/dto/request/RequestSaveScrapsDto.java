package org.likelion.newsfactbackend.domain.scraps.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RequestSaveScrapsDto {
    private String company;
    private String title;
    private String date;
    private String thumbNailUrl;
    private String newsUrl;
    private String companyLogo;
    private String category;
}
