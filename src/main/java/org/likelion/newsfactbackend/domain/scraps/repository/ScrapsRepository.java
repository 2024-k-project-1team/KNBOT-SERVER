package org.likelion.newsfactbackend.domain.scraps.repository;

import org.likelion.newsfactbackend.domain.scraps.domain.Scraps;
import org.likelion.newsfactbackend.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapsRepository extends JpaRepository<Scraps, Long> {
    Page<Scraps> findByUser(User user, Pageable pageable);
    Scraps findByIdAndUser(Long id, User user);
    boolean existsByUserAndNewsUrl(User user, String newsUrl);

}
