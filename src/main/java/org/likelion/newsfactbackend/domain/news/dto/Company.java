package org.likelion.newsfactbackend.domain.news.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Company {
    String companyName;
    public boolean isCompany(){
        return false;
    }

    public boolean isEmpty(){
        return companyName == null || companyName.length() == 0;
    }
}
