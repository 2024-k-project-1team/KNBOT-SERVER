package org.likelion.newsfactbackend.domain.keywords.controller;

import lombok.RequiredArgsConstructor;
import org.likelion.newsfactbackend.domain.keywords.service.KeywordsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/keywords")
@RequiredArgsConstructor
public class KeywordsController {

    private final KeywordsService keywordsService;

    @GetMapping()
    public ResponseEntity<?> getKeywords(){

        try {
            ResponseEntity<?> keywords = keywordsService.getKeywords();
/*            if (keywords == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("키워드를 찾을 수 없습니다");
            }*/
            return ResponseEntity.ok(keywords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("키워드를 조회하는 중에 오류가 발생했습니다.");
        }
    }
}
