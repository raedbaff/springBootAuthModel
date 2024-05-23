package com.securityModel.repository;


import com.securityModel.models.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LikeRepository extends JpaRepository<LikePost,Long> {
    public List<LikePost> findAllByLikepostId(Long id);
    public void deleteAllBylikepostId(Long id);
}
