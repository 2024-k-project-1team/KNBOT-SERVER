package org.likelion.newsfactbackend.domain.chat.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.chat.dao.ChatDAO;
import org.likelion.newsfactbackend.domain.chat.domain.ChatContents;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestChatContentsDto;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestCreateChatRoomDto;
import org.likelion.newsfactbackend.domain.chat.dto.request.RequestUserInfoDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseChatDto;
import org.likelion.newsfactbackend.domain.chat.dto.response.ResponseMyChatRoomsDto;
import org.likelion.newsfactbackend.domain.chat.service.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatDAO chatDAO;

    @Value("${ai.server.host}")
    private String AI_HOST;

    @Override
    public ResponseEntity<?> createChatRoom(RequestUserInfoDto userInfoDto) {
        return chatDAO.createChatRoom(userInfoDto);
    }

    @Override
    public ResponseEntity<?> exitChatRoom(Long roomNumber) {
        return chatDAO.deleteChatRoom(roomNumber);
    }

    @Override
    public ResponseChatDto sendContents(RequestChatContentsDto message, Long roomNumber, String nickname) throws JsonProcessingException {
        String messageType = message.getType().toString();
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Content-Type을 JSON으로 설정
        requestBody.put("question", message.getQuestion());

        ResponseChatDto responseChatDto = new ResponseChatDto();

        switch (messageType){
            case "SEND":
                log.info("[chat service] question : {}", message.getQuestion());

                HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
                try{
                    ResponseEntity<String> response = restTemplate.postForEntity(AI_HOST, requestEntity, String.class);
                    log.info("[chat] ai response : {}", response.getBody());
                    String answer = parseAnswer(response.getBody());
                    return chatDAO.saveMessage(message,roomNumber,answer, nickname);
                }catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    @Override
    public Page<ChatContents> getChatContents(String nickname, Pageable pageable, Long roomNumber) {
        return chatDAO.getChatContents(nickname,pageable,roomNumber);
    }

    @Override
    public Page<ResponseMyChatRoomsDto> getMyChatRooms(String nickname, Pageable pageable) {
        return chatDAO.getMyRooms(nickname, pageable);
    }

    private static String parseAnswer(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            return rootNode.path("answer").asText(); // answer 필드 추출
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
