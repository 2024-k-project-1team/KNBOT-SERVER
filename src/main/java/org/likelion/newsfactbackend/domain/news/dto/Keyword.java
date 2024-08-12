package org.likelion.newsfactbackend.domain.news.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Keyword {
    public String keywordName;

    public Boolean isOver() {
        return keywordName.length() < 255;
    }
}
