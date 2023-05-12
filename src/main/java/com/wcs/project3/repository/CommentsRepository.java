package com.wcs.project3.repository;

import com.wcs.project3.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
