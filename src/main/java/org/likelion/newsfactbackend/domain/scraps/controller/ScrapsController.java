package org.likelion.newsfactbackend.domain.scraps.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestSaveScrapsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestScrapsNewsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.response.ResponseScrapsNewsDto;
import org.likelion.newsfactbackend.domain.scraps.service.ScrapsService;
import org.likelion.newsfactbackend.global.domain.enums.ResultCode;
import org.likelion.newsfactbackend.global.security.JwtTokenProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scraps")
@RequiredArgsConstructor
@Slf4j
public class ScrapsController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ScrapsService scrapsService;

    @Operation(summary = "스크랩 뉴스 조회" , description = "유저가 저장한 뉴스들을 반환합니다.")
    @GetMapping("/news")
    public ResponseEntity<?> getScraps(@RequestParam(defaultValue = "6") int size,
                                       @RequestParam(defaultValue = "0") int page,
                                       HttpServletRequest request){
        //2. 필드의 범위 확인
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("페이지는 0 이상이어야 하며, 크기는 0보다 커야 합니다.");
        }

        String token = jwtTokenProvider.extractToken(request);
        if(token==null){
            return ResultCode.TOKEN_IS_NULL.toResponseEntity();
        }
        String nickName = jwtTokenProvider.getUserNickName(token);
        if(nickName==null){
            return ResultCode.NOT_FOUND_USER.toResponseEntity();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<ResponseScrapsNewsDto> scraps = scrapsService.getScrapsByPage(pageable, nickName);

        return ResponseEntity.status(HttpStatus.OK).body(scraps);
    }


    @Operation(summary = "뉴스 스크랩",description = "유저가 읽고 있는 기사를 저장합니다.")
    @PostMapping()
    public ResponseEntity<?> createScrap(@RequestBody RequestSaveScrapsDto requestSaveScrapsDto, HttpServletRequest request) {

        try{
            log.info("[scraps] dto : {}", requestSaveScrapsDto.toString());
        }catch (Exception e){
            e.printStackTrace();
        }


        if (requestSaveScrapsDto.getCompany().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Company 값이 없습니다.");
        }
        if (requestSaveScrapsDto.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("제목이 없습니다.");
        }

        String token = jwtTokenProvider.extractToken(request);
        if(token==null){
            return ResultCode.TOKEN_IS_NULL.toResponseEntity();
        }
        String nickName = jwtTokenProvider.getUserNickName(token);
        if(nickName==null){
            return ResultCode.NOT_FOUND_USER.toResponseEntity();
        }

        try {
            Boolean isSave = scrapsService.scrapNews(requestSaveScrapsDto, nickName);

            if (isSave) {
                return ResponseEntity.status(HttpStatus.OK).body("뉴스를 성공적으로 저장하였습니다.");
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 문제가 발생하였습니다.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("뉴스를 저장하는 중에 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "스크랩 뉴스 삭제", description = "유저가 저장한 뉴스를 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteScrap(@RequestParam Long id, HttpServletRequest request) {

        //id가 null값이면 예외처리
        if (id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력 값이 잘못되었습니다.");

        String token = jwtTokenProvider.extractToken(request);
        if(token==null){
            return ResultCode.TOKEN_IS_NULL.toResponseEntity();
        }
        String nickName = jwtTokenProvider.getUserNickName(token);
        if(nickName==null){
            return ResultCode.NOT_FOUND_USER.toResponseEntity();
        }

        //예외처리에 걸리지 않을 시 Delete
        try {
            if(scrapsService.deleteScrap(id, nickName)){
                return ResponseEntity.status(HttpStatus.OK).body("뉴스를 성공적으로 삭제하였습니다.");
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 문제가 발생하였습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("뉴스를 삭제하는 중에 오류가 발생했습니다.");
        }
    }
}
