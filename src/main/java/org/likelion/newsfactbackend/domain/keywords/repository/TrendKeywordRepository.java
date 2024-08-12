package org.likelion.newsfactbackend.domain.keywords.repository;

import org.likelion.newsfactbackend.domain.keywords.domain.TrendKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendKeywordRepository extends JpaRepository<TrendKeyword, Long> {

}
