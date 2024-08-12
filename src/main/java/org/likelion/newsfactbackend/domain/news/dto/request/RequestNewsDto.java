package org.likelion.newsfactbackend.domain.news.dto.request;

import lombok.*;
import org.likelion.newsfactbackend.domain.news.dto.Company;
import org.likelion.newsfactbackend.domain.news.dto.Keyword;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class RequestNewsDto {
    private Keyword keyword;
    private Company company;

    public RequestNewsDto(Keyword keyword, Company company) {
        this.keyword = keyword;
        this.company = company;
    }
}


