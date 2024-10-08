package org.likelion.newsfactbackend.domain.chat.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class ChatContents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime sendTime;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, length = 2048)
    private String aiResponse;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Long chatRoomId;
}
