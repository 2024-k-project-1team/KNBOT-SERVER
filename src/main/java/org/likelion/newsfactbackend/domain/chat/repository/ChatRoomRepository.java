package org.likelion.newsfactbackend.domain.chat.repository;

import org.likelion.newsfactbackend.domain.chat.domain.ChatRoom;
import org.likelion.newsfactbackend.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Page<ChatRoom> findByIdIn(List<Long> roomIds, Pageable pageable);
    Boolean existsByUser(User user);
    Boolean existsByUserAndId(User user, Long roomId);
    List<ChatRoom> findAllByUser(User user);
}
