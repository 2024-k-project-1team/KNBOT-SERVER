package org.likelion.newsfactbackend.domain.keywords.domain;

import jakarta.persistence.*;
import lombok.*;
import org.likelion.newsfactbackend.global.domain.BaseTimeEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table
public class TrendKeyword extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rank;

    @Column(nullable = false, unique = true)
    private String keyword;

    @Column(nullable = false)
    private String count;
}
