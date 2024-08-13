package org.likelion.newsfactbackend.domain.chat.dao;
import org.likelion.newsfactbackend.domain.chat.domain.ChatContents;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestChatContentsDto;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestCreateChatRoomDto;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestUserInfoDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseChatDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseMyChatRoomsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ChatDAO {
    /**
     * 사용자 + 공방관계자 채팅방 생성
     * @param userInfoDto
     * @param
     * @return 상태반환
     */
    ResponseEntity<?> createChatRoom(RequestUserInfoDto userInfoDto);

    /**
     * 채팅방 삭제
     * @param roomNumber
     * @return 상태반환
     */
    ResponseEntity<?> deleteChatRoom(Long roomNumber);

    /**
     * 메세지 전송 후 저장
     * @param requestChatContentsDto
     * @param roomNumber
     * @return dto
     */
    ResponseChatDto saveMessage(RequestChatContentsDto requestChatContentsDto, Long roomNumber, String answer, String nickname);

    /**
     * 채팅방 내용 반환
     * @param nickname
     * @param pageable
     * @param roomNumber
     * @return page
     */
    Page<ChatContents> getChatContents(String nickname, Pageable pageable, Long roomNumber);

    /**
     * 사용자가 속한 채팅방을 조회
     * @param nickname
     * @param pageable
     * @return
     */
    Page<ResponseMyChatRoomsDto> getMyRooms(String nickname, Pageable pageable);

}
