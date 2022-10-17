package com.example.blog.mapper;

import com.example.blog.domain.MemberVO;

import java.util.Map;

public interface MemberMapper {
    public boolean sign_in(MemberVO member);
    public boolean isDuplicated(String id);
    public int sign_up(MemberVO member);
    public int updateMember(MemberVO member);
    public int withdrawal(Map<String, String> params);
}
