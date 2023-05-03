package com.wcs.project3.repository;

import com.wcs.project3.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findUser_FavoriteArticlesByUsersId(Long users_id);
}

