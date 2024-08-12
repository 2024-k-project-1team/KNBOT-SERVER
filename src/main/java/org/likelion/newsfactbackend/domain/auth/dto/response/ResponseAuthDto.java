package org.likelion.newsfactbackend.domain.auth.dto.response;


import lombok.*;
import org.likelion.newsfactbackend.global.domain.CommonResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseAuthDto {
    private String accessToken;
    private String name;
    private CommonResponse<?> status;
}
