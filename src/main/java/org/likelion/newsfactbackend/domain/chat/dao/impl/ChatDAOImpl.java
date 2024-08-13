package org.likelion.newsfactbackend.domain.chat.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.chat.dao.ChatDAO;
import org.likelion.newsfactbackend.domain.chat.domain.ChatContents;
import org.likelion.newsfactbackend.domain.chat.domain.ChatRoom;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestChatContentsDto;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestUserInfoDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseChatDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseMyChatRoomsDto;
import org.likelion.newsfactbackend.domain.chat.repository.ChatContentsRepository;
import org.likelion.newsfactbackend.domain.chat.repository.ChatRoomRepository;
import org.likelion.newsfactbackend.global.domain.enums.ResultCode;
import org.likelion.newsfactbackend.global.exception.ChatRoomNotFoundException;
import org.likelion.newsfactbackend.global.exception.NotMemberException;
import org.likelion.newsfactbackend.user.domain.User;
import org.likelion.newsfactbackend.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatDAOImpl implements ChatDAO {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatContentsRepository chatContentsRepository;
    @Override
    public ResponseEntity<?> createChatRoom(RequestUserInfoDto userInfoDto) {
        log.info("[chat] create chat room");
        String userEmail = userInfoDto.getUserEmail();
        String userNickname = userInfoDto.getUserNickname();

        if (checkUserExist(userEmail,userNickname)){
            log.info("[chat] user is exist!");
            User buyer = userRepository.findByEmailAndNickName(userEmail,userNickname);

            log.info("[chat] is check chatroom exist....");
            ChatRoom newChatRoom = new ChatRoom();
            newChatRoom.setRoomName("새 채팅방");
            newChatRoom.setUser(buyer);

            chatRoomRepository.save(newChatRoom);

            log.info("[chat] create chatroom success");

            return ResultCode.OK.toResponseEntity();
        }else{
            log.warn("[chat] user is not found!");
            return ResultCode.NOT_FOUND_USER.toResponseEntity();
        }
    }

    @Override
    public ResponseEntity<?> deleteChatRoom(Long roomId) {
        log.info("[chat] find chatroom");
        Optional<ChatRoom> chatRoomList = chatRoomRepository.findById(roomId);
        if(!chatRoomList.isEmpty()){

            List<ChatContents> chatContentsList = chatContentsRepository.findAllByChatRoomId(roomId);

            chatContentsRepository.deleteAll(chatContentsList); // 채팅 내역 삭제

            ChatRoom findChatRoom = chatRoomList.get();

            findChatRoom.setUser(null);

            chatRoomRepository.delete(findChatRoom);
            return ResultCode.OK.toResponseEntity();
        }else{
            return ResultCode.NOT_FOUND_CHATROOM.toResponseEntity();
        }
    }

    @Override
    public ResponseChatDto saveMessage(RequestChatContentsDto requestChatContentsDto,
                                       Long roomId,
                                       String answer,
                                       String nickname) {

        User user = userRepository.findByNickName(nickname);

        // 저장되는 채팅 내역
        ChatContents chatContents = ChatContents.builder()
                .sendTime(LocalDateTime.now())
                .sender(nickname)
                .message(requestChatContentsDto.getQuestion())
                .aiResponse(answer)
                .chatRoomId(roomId)
                .build();

        chatContentsRepository.save(chatContents);

        // 실제 전송되는 메세지
        return ResponseChatDto.builder()
                .question(chatContents.getMessage())
                .nickName(chatContents.getSender())
                .time(chatContents.getSendTime())
                .answer(chatContents.getAiResponse())
                .build();
    }

    @Override
    public Page<ChatContents> getChatContents(String nickname, Pageable pageable, Long roomNumber) {
        log.info("[chat] get chat contents room number : {}",roomNumber);
        User user = userRepository.findByNickName(nickname);
        Optional<ChatRoom> chatRoomList = chatRoomRepository.findById(roomNumber);
        if(!chatRoomList.isEmpty()){
            ChatRoom chatRoom = chatRoomList.get();
            if(checkMember(chatRoom.getId(), user)){
                return chatContentsRepository.findByChatRoomId(roomNumber,pageable);
            }else{
                throw new NotMemberException("채팅방 조회 권한이 없습니다.");
            }
        }else{
            throw new ChatRoomNotFoundException("채팅방이 존재하지 않습니다.");
        }
    }

    @Override
    public Page<ResponseMyChatRoomsDto> getMyRooms(String nickname, Pageable pageable) {
        log.info("[chat] get" + "{}" + "chat rooms",nickname);
        User user = userRepository.findByNickName(nickname);
        if(!(user == null)){
            List<ChatRoom> chatRoomList = chatRoomRepository.findAllByUser(user);
            if(chatRoomList.isEmpty()){
                    return Page.empty(pageable);
                }
            log.info("[chat] chatroom is exist!");
            List<Long> roomIds = chatRoomList.stream().map(chatroom -> chatroom.getId())
                    .collect(Collectors.toList());
            log.info("[chat] my chat room ids : {}", Arrays.asList(roomIds).toString());
            log.info("[chat] collect my chat rooms..");

            Page<ChatRoom> chatRooms = chatRoomRepository.findByIdIn(roomIds, pageable);

            List<ResponseMyChatRoomsDto> chatRoomsDtoList = chatRooms.stream()
                    .map(chatRoom -> {
                        ResponseMyChatRoomsDto dto = new ResponseMyChatRoomsDto();
                        dto.setId(chatRoom.getId());
                        dto.setRoomName(chatRoom.getRoomName());
                        dto.setUpdateAt(chatRoom.getUpdatedAt());
                        return dto;
                    }).collect(Collectors.toList());

            return new PageImpl<>(chatRoomsDtoList, pageable, chatRooms.getTotalElements());

            }else{
            log.error("[chat] not found user");
            throw new NotMemberException();
        }

    }


    private boolean checkUserExist(String userEmail, String userNickname){
        log.info("[chat] check user exist");
        return userRepository.existsByEmailAndNickName(userEmail,userNickname);
    }
    public boolean checkMember(Long roomId, User member){
        return chatRoomRepository.existsByUserAndId(member,roomId);
    }
}
