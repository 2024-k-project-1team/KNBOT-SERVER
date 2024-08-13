package org.likelion.newsfactbackend.domain.chat.dto.response;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseChatDto {
   private String question;
   private String answer;
   private String nickName;
   private LocalDateTime time;
}
