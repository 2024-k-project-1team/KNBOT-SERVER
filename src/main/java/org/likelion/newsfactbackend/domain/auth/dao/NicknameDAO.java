package org.likelion.newsfactbackend.domain.auth.dao;

public interface NicknameDAO {
    /**
     *
     * @return 닉네임 생성 ex) NEWSPECT#1
     */
    String generateNickName();
}
