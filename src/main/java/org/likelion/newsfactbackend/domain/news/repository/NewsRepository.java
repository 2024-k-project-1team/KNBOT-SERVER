package org.likelion.newsfactbackend.domain.news.repository;

import org.likelion.newsfactbackend.domain.news.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News,Long> {

}
