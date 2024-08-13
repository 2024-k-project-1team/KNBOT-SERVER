package org.likelion.newsfactbackend.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.likelion.newsfactbackend.domain.chat.domain.ChatContents;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestChatContentsDto;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestCreateChatRoomDto;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestUserInfoDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseChatDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseMyChatRoomsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ChatService {
    /**
     * 사용자 + 공방관계자 채팅방 생성
     * @param userInfoDto
     * @param
     * @return
     */
    ResponseEntity<?> createChatRoom(RequestUserInfoDto userInfoDto);

    /**
     * 채팅방 삭제
     * @param roomNumber 채팅방 id 값
     * @return
     */
    ResponseEntity<?> exitChatRoom(Long roomNumber);

    /**
     * 메세지 전송
     * @param message
     * @param roomNumber 채팅방 id 값
     * @return ResponseChatDto
     */
    ResponseChatDto sendContents(RequestChatContentsDto message, Long roomNumber, String nickname) throws JsonProcessingException;

    /**
     * 채팅방 내역 페이징 처리 후 반환
     * @param nickname
     * @param pageable
     * @param roomId
     * @return
     */
    Page<ChatContents> getChatContents(String nickname, Pageable pageable, Long roomId);

    /**
     * 내가 속한 채팅방들 반환
     * @param nickname
     * @return 채팅방
     */
    Page<ResponseMyChatRoomsDto> getMyChatRooms(String nickname, Pageable pageable);
}
