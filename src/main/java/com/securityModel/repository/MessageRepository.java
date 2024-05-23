package com.securityModel.repository;

import com.securityModel.models.Message;
import com.securityModel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByReceiverOrSenderOrderByTimestampDesc(User user, User user1);
    List<Message> findBySender_IdAndReceiver_IdOrSender_IdAndReceiver_IdOrderByTimestampAsc(Long userId1, Long userId2, Long userId3, Long userId4);

}
