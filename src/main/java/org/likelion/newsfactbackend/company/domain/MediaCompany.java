package org.likelion.newsfactbackend.company.domain;

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
public class MediaCompany extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String logoUrl;

    @Column(nullable = false, unique = true)
    private String url;
}
