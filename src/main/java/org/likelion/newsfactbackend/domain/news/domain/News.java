package org.likelion.newsfactbackend.domain.news.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String company;

    @Column(nullable = false, unique = false)
    private String date;

    @Column(nullable = false, unique = true)
    private String img;

    @Column(nullable = false, unique = true)
    private String content;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = false)
    private String writer;

    @Column(nullable = false, unique = false)
    private String writerEmail;

}
