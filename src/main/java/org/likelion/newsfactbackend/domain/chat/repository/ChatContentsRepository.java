package org.likelion.newsfactbackend.domain.chat.repository;


import org.likelion.newsfactbackend.domain.chat.domain.ChatContents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatContentsRepository extends JpaRepository<ChatContents, Long> {
    Page<ChatContents> findByChatRoomId(Long roomNumber, Pageable pageable);
    List<ChatContents> findAllByChatRoomId(Long roomNumber);
}
