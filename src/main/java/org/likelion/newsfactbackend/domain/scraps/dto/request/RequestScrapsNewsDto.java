package org.likelion.newsfactbackend.domain.scraps.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestScrapsNewsDto {
    private Integer page;
    private Integer size;
}
