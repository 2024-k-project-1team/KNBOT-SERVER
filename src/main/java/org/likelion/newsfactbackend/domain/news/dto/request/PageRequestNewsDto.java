package org.likelion.newsfactbackend.domain.news.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageRequestNewsDto {
    private Long size;
    private String keyword;

    public PageRequestNewsDto(Long size, String keyword) {
        this.size = size;
        this.keyword = keyword;
    }
    public boolean isOverSize(Long size) {
        return size > 14;
    }
}
