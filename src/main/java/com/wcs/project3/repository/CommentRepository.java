package com.wcs.project3.repository;

import com.wcs.project3.entity.Comment;
import com.wcs.project3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUser(User userToDelete);

    void deleteByUser(User user);
}
