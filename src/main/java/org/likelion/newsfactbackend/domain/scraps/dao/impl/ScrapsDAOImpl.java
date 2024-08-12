package org.likelion.newsfactbackend.domain.scraps.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.scraps.dao.ScrapsDAO;
import org.likelion.newsfactbackend.domain.scraps.domain.Scraps;
import org.likelion.newsfactbackend.domain.scraps.dto.request.RequestSaveScrapsDto;
import org.likelion.newsfactbackend.domain.scraps.dto.response.ResponseScrapsNewsDto;
import org.likelion.newsfactbackend.domain.scraps.repository.ScrapsRepository;
import org.likelion.newsfactbackend.user.domain.User;
import org.likelion.newsfactbackend.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScrapsDAOImpl implements ScrapsDAO {
    private final ScrapsRepository scrapsRepository;
    private final UserRepository userRepository;
    @Override
    public boolean saveNews(RequestSaveScrapsDto requestSaveScrapsDto, String nickname) {
        User user = userRepository.findByNickName(nickname);
        if(user==null || !user.isEnabled()){ // 유저 존재하지 않음.
            return false;
        }else{
            if(scrapsRepository.existsByUserAndNewsUrl(user, requestSaveScrapsDto.getNewsUrl())){
                return false;
            }
            try{
                log.warn("[scraps] saving news...");
                Scraps scraps = Scraps.builder()
                        .company(requestSaveScrapsDto.getCompany())
                        .title(requestSaveScrapsDto.getTitle())
                        .date(requestSaveScrapsDto.getDate())
                        .thumbNailUrl(requestSaveScrapsDto.getThumbNailUrl())
                        .newsUrl(requestSaveScrapsDto.getNewsUrl())
                        .user(user)
                        .companyLogo(requestSaveScrapsDto.getCompanyLogo())
                        .category(requestSaveScrapsDto.getCategory())
                        .createdAt(LocalDateTime.now())
                        .build();

                scrapsRepository.save(scraps);

                log.info("[scraps] save news successfully");
                return true;

            }catch (Exception e){
                log.error("[scraps] error occurred saving news...");
                e.printStackTrace();
                return false;
            }
        }
    }
    @Override
    public Page<ResponseScrapsNewsDto> getMyNews(Pageable pageable, String nickname) {
        log.info("[scraps] get {}'s news", nickname);
        User user = userRepository.findByNickName(nickname);
        if(!(user == null)){
            Page<Scraps> scrapsPage = scrapsRepository.findByUser(user, pageable);

            List<ResponseScrapsNewsDto> dtos = scrapsPage.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return new PageImpl<>(dtos, pageable, scrapsPage.getTotalElements());
        }else{
            return Page.empty(pageable);
        }
    }

    @Override
    public boolean deleteNews(Long id, String nickName) {
        log.info("[scraps] delete {}'s news..", nickName);
        User user = userRepository.findByNickName(nickName);

        if(!(user==null)){
            Scraps scraps = scrapsRepository.findByIdAndUser(id, user);
            if(!(scraps==null)){
                log.info("[scraps] deleting...");
                try{
                    scraps.setUser(null);
                    scrapsRepository.delete(scraps);
                    log.info("[scraps] delete successfully!");
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("[scraps] delete fail!");
                    return false;
                }
            }else{
             return false;
            }
        }else{
            return false;
        }
    }

    private ResponseScrapsNewsDto convertToDto(Scraps scraps) {
        return ResponseScrapsNewsDto.builder()
                .id(scraps.getId())
                .company(scraps.getCompany())
                .title(scraps.getTitle())
                .date(scraps.getDate())
                .thumbNailUrl(scraps.getThumbNailUrl())
                .newsUrl(scraps.getNewsUrl())
                .companyLogo(scraps.getCompanyLogo())
                .category(scraps.getCategory())
                .build();
    }
}
