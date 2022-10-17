package com.springboot.blog_boot.result;

import com.springboot.blog_boot.domain.MemberVO;

public class UpdateMemberResult {
    private MemberVO member;
    private boolean result;

    public UpdateMemberResult(MemberVO member, boolean result) {
        this.member = member;
        this.result = result;
    }

    public MemberVO getMember() {
        return member;
    }

    public void setMember(MemberVO member) {
        this.member = member;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
