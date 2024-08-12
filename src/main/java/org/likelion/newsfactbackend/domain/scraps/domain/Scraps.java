package org.likelion.newsfactbackend.domain.scraps.domain;

import jakarta.persistence.*;
import lombok.*;
import org.likelion.newsfactbackend.user.domain.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table
public class Scraps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String company;

    @Column(nullable = false, unique = false)
    private String title;

    @Column(nullable = false, unique = false)
    private String date;

    @Column(nullable = false, unique = false)
    private String thumbNailUrl;

    @Column(nullable = false, unique = false)
    private String newsUrl;

    @Column(nullable = false, unique = false)
    private String companyLogo;

    @Column(nullable = false, unique = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
