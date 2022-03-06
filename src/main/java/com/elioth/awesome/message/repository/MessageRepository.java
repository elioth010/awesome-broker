package com.elioth.awesome.message.repository;

import com.elioth.awesome.message.repository.entity.MessageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends PagingAndSortingRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m WHERE m.fromUser.id = :userId")
    List<MessageEntity> findAllMessagesSentByUser(@Param("userId") Long userId);

    @Query("SELECT m FROM MessageEntity m WHERE m.toUser.id = :userId")
    List<MessageEntity> findAllMessagesReceivedByUser(@Param("userId") Long userId);
}
