package com.securityModel.repository;

import com.securityModel.models.Notifications;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface NotificationsRepository extends JpaRepository<Notifications,Long> {
    public List<Notifications> findNotificationsByUserUsername(String username);
}
