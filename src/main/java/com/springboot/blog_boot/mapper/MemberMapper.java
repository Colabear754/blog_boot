package com.springboot.blog_boot.mapper;


import com.springboot.blog_boot.domain.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MemberMapper {
    public boolean sign_in(MemberVO member);
    public boolean isDuplicated(String id);
    public int sign_up(MemberVO member);
    public int updateMember(MemberVO member);
    public int withdrawal(Map<String, String> params);
}
