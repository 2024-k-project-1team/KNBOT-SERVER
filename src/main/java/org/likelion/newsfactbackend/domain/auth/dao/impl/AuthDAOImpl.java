package org.likelion.newsfactbackend.domain.auth.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.auth.dao.AuthDAO;
import org.likelion.newsfactbackend.domain.auth.dao.NicknameDAO;
import org.likelion.newsfactbackend.domain.auth.dto.request.RequestSaveUserDto;
import org.likelion.newsfactbackend.domain.auth.dto.response.ResponseAuthDto;
import org.likelion.newsfactbackend.global.config.RedisConfig;
import org.likelion.newsfactbackend.global.domain.CommonResponse;
import org.likelion.newsfactbackend.global.domain.enums.ResultCode;
import org.likelion.newsfactbackend.global.security.JwtTokenProvider;
import org.likelion.newsfactbackend.user.domain.User;
import org.likelion.newsfactbackend.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthDAOImpl implements AuthDAO {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NicknameDAO nicknameDAO;
    private final RedisConfig redisConfig;

    @Override
    public ResponseEntity<?> login(RequestSaveUserDto requestSaveUserDto) {
        log.info("[sign up] requestsavedto : {}", requestSaveUserDto.toString() );
        // 유저 회원가입 여부 확인
        if (checkUserExist(requestSaveUserDto.getEmail(), requestSaveUserDto.getLoginType())) {
            log.info("[sign up] found user!");
            // 소셜 로그인 타입 확인
            User user = userRepository.findByEmailAndLoginType(requestSaveUserDto.getEmail(), requestSaveUserDto.getLoginType());
            // 회원 탈퇴 유저 여부 확인
            if (user.isEnabled()) {
                String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getNickName());
                try{
                    log.info("[auth] saving refresh token in redis..");
                    // refresh token redis 저장
                    redisConfig.redisTemplate().opsForValue().set(user.getNickName(),
                            refreshToken,
                            jwtTokenProvider.getExpireTime(refreshToken).getTime() - System.currentTimeMillis(),
                            TimeUnit.MILLISECONDS
                    );
                    log.info("[auth] saved refresh token in redis successfully!");
                }catch (Exception e){
                    log.error("[auth] error occurred saving redis data...");
                    e.printStackTrace();
                }
                return ResponseEntity.status(ResultCode.OK.getCode())
                        .body(ResponseAuthDto.builder()
                                .accessToken(jwtTokenProvider.createAccessToken(user.getEmail(), user.getNickName(), user.getRoles()))
                                .name(user.getName())
                                .status(CommonResponse.success())
                                .build());
            } else {
                return ResultCode.DELETED_USER.toResponseEntity();
            }
        } else {
            log.info("[sign up] no user");
            log.info("[auth] sign up");
            // user 저장
            User user = User.builder()
                    .name(requestSaveUserDto.getName())
                    .nickName(nicknameDAO.generateNickName())
                    .password(requestSaveUserDto.getPassword())
                    .email(requestSaveUserDto.getEmail())
                    .profileUrl(requestSaveUserDto.getProfileUrl())
                    .loginType(requestSaveUserDto.getLoginType())
                    .useAble(requestSaveUserDto.isUseAble())
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            int retryCount = 0;
            final int maxRetries = 5; // 재시도 횟수 제한
            while(retryCount < maxRetries){
                try {
                    userRepository.save(user);
                    log.info("[auth] signup success");

                    String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getNickName());

                    log.info("[auth] saving refresh token in redis..");
                    redisConfig.redisTemplate().opsForValue().set(user.getNickName(),
                            refreshToken,
                            jwtTokenProvider.getExpireTime(refreshToken).getTime() - System.currentTimeMillis(),
                            TimeUnit.MILLISECONDS
                    );
                    log.info("[auth] saved refresh token in redis successfully!");

                    return ResponseEntity.status(ResultCode.OK.getCode())
                            .body(ResponseAuthDto.builder()
                                    .accessToken(jwtTokenProvider.createAccessToken(user.getEmail(), user.getNickName(), user.getRoles()))
                                    .name(user.getName())
                                    .status(CommonResponse.success())
                                    .build());
                } catch (DataIntegrityViolationException e) { // 중복 시 재설정 후 다시 save
                    log.warn("[auth] signup failed due to duplicate nickname, retrying...");
                    user.setNickName(nicknameDAO.generateNickName());
                    retryCount++;
                }
            }
            log.error("[auth] signup failed due to max retries");
            return ResultCode.DUPLICATION_NICKNAME.toResponseEntity();
        }
    }
    @Override
    public ResponseEntity<?> deleteToken(String token){
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        log.info("[auth] logout user name : {}", authentication.getName());

        if (redisConfig.redisTemplate().opsForValue().get(authentication.getName())!=null){ // refresh token 이 있을 경우

            redisConfig.redisTemplate().delete(authentication.getName()); // refresh token 삭제

            redisConfig.redisTemplate().opsForValue().set(token, // save {token : logout}
                    "logout",
                    jwtTokenProvider.getExpireTime(token).getTime() - System.currentTimeMillis(),
                    TimeUnit.MILLISECONDS);

            return ResultCode.OK.toResponseEntity();
        }
        return ResultCode.NOT_IN_STORAGE.toResponseEntity();
    }

    private boolean checkUserExist(String email, String loginType){
        log.info("[auth] check user exist..");
        return userRepository.existsByEmailAndLoginType(email, loginType);
    }
}
