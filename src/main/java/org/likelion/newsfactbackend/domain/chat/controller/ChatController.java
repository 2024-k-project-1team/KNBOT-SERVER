package org.likelion.newsfactbackend.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.chat.domain.ChatRoom;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestChatContentsDto;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestUserInfoDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseChatDto;
import org.likelion.newsfactbackend.domain.chat.repository.ChatRoomRepository;
import org.likelion.newsfactbackend.domain.chat.service.ChatService;
import org.likelion.newsfactbackend.global.domain.enums.ResultCode;
import org.likelion.newsfactbackend.global.exception.ChatRoomNotFoundException;
import org.likelion.newsfactbackend.global.exception.NotFoundUserException;
import org.likelion.newsfactbackend.global.security.JwtTokenProvider;
import org.likelion.newsfactbackend.user.domain.User;
import org.likelion.newsfactbackend.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.xml.transform.Result;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;
    private final SimpMessagingTemplate template;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Operation(summary = "채팅방을 새로 생성합니다.", security = @SecurityRequirement(name="JWT"))
    @PostMapping("/new")
    public ResponseEntity<?> createChatRoom(
            HttpServletRequest request){
        String token = jwtTokenProvider.extractToken(request);

        Map<String, String> userInfoMap = jwtTokenProvider.getUserInfo(token);
        log.info("[chat] user info : {}", userInfoMap.toString());

        RequestUserInfoDto userInfoDto = RequestUserInfoDto.builder()
                .userEmail(userInfoMap.get("email")).userNickname(userInfoMap.get("nickname"))
                .build();

        return chatService.createChatRoom(userInfoDto);
    }

    @Operation(summary = "채팅방 제목을 변경합니다.")
    @PostMapping("/rename/{roomNumber}")
    public ResponseEntity<?> changeChatRoomName(@RequestBody String roomName, @RequestParam Long roomNumber){
        Optional<ChatRoom> chatRoomList = chatRoomRepository.findById(roomNumber);
        if(chatRoomList.isEmpty()){
            return ResultCode.NOT_FOUND_CHATROOM.toResponseEntity();
        }
        if(roomName.isEmpty()){
            return ResultCode.NOT_FOUND_CHATROOM_NAME.toResponseEntity();
        }
        if(roomNumber == null){
            return ResultCode.NOT_FOUND_CHATROOM_NUMBER.toResponseEntity();
        }

        ChatRoom chatRoom = chatRoomList.get();

        chatRoom.setRoomName(roomName);

        chatRoomRepository.save(chatRoom);

        return ResultCode.OK.toResponseEntity();
    }

    @Operation(summary = "채팅방을 나갑니다.", security = @SecurityRequirement(name = "JWT"))
    @DeleteMapping("/out/{roomNumber}")
    public ResponseEntity<?> removeChatRoom(
            HttpServletRequest request,
            @PathVariable Long roomNumber){

        return chatService.exitChatRoom(roomNumber);
    }
    @Operation(summary = "채팅방 내역을 조회합니다.", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/contents/{roomNumber}")
    public ResponseEntity<?> getChatRoom(
            HttpServletRequest request,
            @RequestParam int page,
            @PathVariable Long roomNumber){

        Pageable pageable = PageRequest.of(page,10, Sort.Direction.DESC,"sendTime");

        String token = jwtTokenProvider.extractToken(request);
        String nickName = jwtTokenProvider.getUserNickName(token);

        return ResponseEntity.ok(chatService.getChatContents(nickName,pageable,roomNumber));
    }
    @Operation(summary = "내가 속한 채팅방 조회", description = "내가 생성한 채팅방 조회 후 반환합니다.")
    @GetMapping("/my-rooms")
    public ResponseEntity<?> getMyChatRooms(HttpServletRequest request,
                                            @RequestParam(defaultValue = "0") int page){
        String token = jwtTokenProvider.extractToken(request);
        String nickName = jwtTokenProvider.getUserNickName(token);

        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC,"createdAt");

        return ResponseEntity.ok(chatService.getMyChatRooms(nickName, pageable));
    }

    @MessageMapping("/knbot/{roomNumber}") // mapping ex)/pub/knbot/{roomnumber}
    public void sendMessage(RequestChatContentsDto message,
                            SimpMessageHeaderAccessor accessor,
                            @DestinationVariable Long roomNumber) throws Exception {

        log.info("[chat] room id : {}",roomNumber);

        String nickname = (String) accessor.getSessionAttributes().get("senderNickname");
        log.info("[chat] check memeber....");
        if (nickname == null) {
            throw new IllegalArgumentException("세션에 닉네임이 없습니다.");
        }

        Long sessionRoomId = (Long) accessor.getSessionAttributes().get("chatRoomId");

        if(sessionRoomId == null){ // 세션에 채팅방이 없으면 검증 시작
            log.info("[chat] chatting session is empty room id : {}, nickname : {}", roomNumber,nickname);
            User user = userRepository.findByNickName(nickname);
            if(user == null){
                throw new NotFoundUserException();
            }
            Optional<ChatRoom> chatRoomList = chatRoomRepository.findById(roomNumber);
            if(chatRoomList.isEmpty()){
                throw new ChatRoomNotFoundException();
            }
            if(checkMember(user, roomNumber)){
                accessor.getSessionAttributes().putIfAbsent("chatRoomId", roomNumber);// 세션에 저장되어 있지 않을때, 세션에 저장
                log.info("[chat] successfully put room id to session");
            }
            log.info("[chat] complete check member");
            Thread.sleep(1000); // 비동기적으로 메시지를 처리하기 위해서 1초 지연(옵션)
            log.info("[chat] question : {}", message.getQuestion());
            ResponseChatDto responseChatDto = chatService.sendContents(message,roomNumber,nickname);
            template.convertAndSend("/sub/chatroom/"+roomNumber,responseChatDto); // 구독하고 있는 채팅방에 전송

        }else { // 이미 검증이 끝났으면 메세지 처리
            log.info("[chat] complete check member");
            Thread.sleep(1000); // 비동기적으로 메시지를 처리하기 위해서 1초 지연(옵션)
            log.info("[chat] question : {}", message.getQuestion());
            ResponseChatDto responseChatDto = chatService.sendContents(message,roomNumber,nickname);
            template.convertAndSend("/sub/chatroom/"+roomNumber,responseChatDto); // 구독하고 있는 채팅방에 전송
        }
    }
    private boolean checkMember(User member, Long roomId){
        return chatRoomRepository.existsByUserAndId(member,roomId);
    }
}
