package org.likelion.newsfactbackend.domain.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.likelion.newsfactbackend.global.domain.BaseTimeEntity;
import org.likelion.newsfactbackend.user.domain.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = false)
    private String roomName;

    @ManyToOne
    @JoinColumn
    private User user; // 한 채팅방에 속하는 유저
}
