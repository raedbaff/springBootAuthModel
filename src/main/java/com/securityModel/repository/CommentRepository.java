package com.securityModel.repository;

import com.securityModel.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment>findCommentsByPostId(Long id);
    public void deleteAllByPost_Id(long id);
}
