package com.wcs.project3.repository;

import com.wcs.project3.entity.Comments;
import com.wcs.project3.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByRecipeOrderByCreatedAtDesc(Long id);


    List<Comments> getCommentsByRecipeId(Long id);
}
