package org.likelion.newsfactbackend.domain.chat.dto.request;

import lombok.*;
import org.likelion.newsfactbackend.global.domain.enums.MessageType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestChatContentsDto {
    private MessageType type;
    private String question;
}
