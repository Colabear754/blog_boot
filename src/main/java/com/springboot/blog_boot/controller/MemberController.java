package com.springboot.blog_boot.controller;

import com.springboot.blog_boot.crypto.Crypto;
import com.springboot.blog_boot.domain.MemberVO;
import com.springboot.blog_boot.mapper.MemberMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.security.GeneralSecurityException;
import java.util.Map;

@Api(tags = "회원 API")
@RestController
@RequestMapping("/members/*")
public class MemberController {
    @Autowired
    private MemberMapper memberDao;
    private final Crypto crypt = new Crypto();

    @ApiOperation("ID 중복확인")
    @ApiImplicitParam(name = "id", value = "중복 확인을 할 계정", required = true)
    @GetMapping("/is-duplicated")
    public String is_duplicated(@RequestParam String id, Model model) {
        // id 중복확인
        model.addAttribute("result", memberDao.isDuplicated(id));

        return "ID 중복 여부 : " + model.getAttribute("result");
    }

    @ApiOperation("회원가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "계정", required = true),
            @ApiImplicitParam(name = "password", value = "비밀번호", required = true),
            @ApiImplicitParam(name = "name", value = "이름", required = true)
    })
    @PostMapping("/sign-up")
    public String sign_up(@ModelAttribute MemberVO member, Model model) throws GeneralSecurityException {
        // 회원가입
        int result = -1;    // 1: 가입성공, 0: 이미 존재하는 회원, -1: 가입실패
        // 회원가입 정보 암호화
        member.setPassword(crypt.encrypt(member.getPassword()));
        member.setName(crypt.encrypt(member.getName()));
        member.setPhone(crypt.encrypt(member.getPhone()));
        member.setDepartment(crypt.encrypt(member.getDepartment()));

        result = memberDao.sign_up(member);

        model.addAttribute("result", result);

        return "회원가입 결과 : " + (result > 0);
    }

    @ApiOperation("회원정보 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "계정", required = true),
            @ApiImplicitParam(name = "password", value = "기존 비밀번호", required = true),
            @ApiImplicitParam(name = "newPassword", value = "새 비밀번호"),
            @ApiImplicitParam(name = "name", value = "이름", required = false)
    })
    @PutMapping("/updateMember")
    public String updateMember(@ModelAttribute MemberVO member, @RequestParam String newPassword, Model model) throws GeneralSecurityException {
        // 회원정보 수정
        int result = -1;    //  1: 수정성공, 0: 해당 정보 없음, -1: 수정실패
        // 회원 정보 암호화
        member.setPassword(crypt.encrypt(member.getPassword()));
        member.setName(crypt.encrypt(member.getName()));
        member.setPhone(crypt.encrypt(member.getPhone()));
        member.setDepartment(crypt.encrypt(member.getDepartment()));
        newPassword = crypt.encrypt(newPassword);

        if (memberDao.sign_in(member)) {    // 비밀번호 체크
            if (newPassword != null && !newPassword.isBlank()) {    // 새로운 비밀번호를 입력했으면
                member.setPassword(newPassword);    // 회원 객체의 비밀번호를 새로운 비밀번호로 변경
            }
            result = memberDao.updateMember(member);    // 회원정보 수정
        }
        
        model.addAttribute("result", result);

        return "회원정보 수정 결과 : " + result;
    }

    @ApiOperation("회원 탈퇴")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "계정", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "비밀번호", required = true, paramType = "query")
    })
    @PostMapping("/withdrawal")
    public String withdrawal(@RequestParam @ApiIgnore Map<String, String> params, Model model) throws GeneralSecurityException {
        // 회원 탈퇴
        int result = -1;    //  1: 탈퇴성공, 0: 해당 정보 없음, -1: 탈퇴실패
        params.put("password", crypt.encrypt(params.get("password")));  // 비밀번호 암호화

        result = memberDao.withdrawal(params);

        model.addAttribute("result", result);

        return "회원 탈퇴 결과 : " + (result > 0);
    }

    @ApiOperation("로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "아이디", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "비밀번호", required = true, paramType = "query", format = "password")
    })
    @PostMapping("/sign-in")
    public String sign_in(@ApiIgnore MemberVO member, @ApiIgnore HttpSession session, Model model) throws GeneralSecurityException {
        // 로그인
        member.setPassword(crypt.encrypt(member.getPassword()));    // 비밀번호 암호화
        boolean result = memberDao.sign_in(member); // 로그인 성공 여부

        if (result) {   // 로그인 성공 시
            session.setAttribute("_uid", member.getId());   // 세션에 ID 설정
        }

        model.addAttribute("result", result);

        return "로그인 결과 : " + result;
    }
}
