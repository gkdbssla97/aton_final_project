package com.example.aton_final_project.controller;

import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.util.AESCipher;
import com.example.aton_final_project.util.constants.LoginConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static com.example.aton_final_project.util.constants.AccountStatus.*;
import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @PostMapping("/login")
//    @ResponseBody
//    public ResponseEntity<LogInResponseDto> login(@RequestBody(required = false) MemberRequestDto memberRequestDto) throws Exception {
//
//        System.out.println(memberRequestDto);
//        AccessTokenDto findAccessToken = memberService.findEncryptKeyByEmail(memberRequestDto.getEmail());
//        System.out.println(findAccessToken);
//
//        if(memberService.isVerifiedMember(memberRequestDto.getEmail(), memberRequestDto.getPassword(),
//                findAccessToken.getMemberId(), findAccessToken.getEncryptKey())) {
//            MemberResponseDto findMemberById = memberService.findMemberById(findAccessToken.getMemberId());
//
//            return new ResponseEntity<>(
//                    memberService.maskingInformationByLogIn(findMemberById.getUsername(), findMemberById.getPhoneNo(), findAccessToken.getEncryptKey()), HttpStatus.OK);
//        }
//
//        throw new Exception("존재하지 않는 회원입니다.");
//    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LogInResponseDto> loginV2(@RequestBody(required = false) MemberRequestDto memberRequestDto,
                                                    HttpServletResponse response, HttpServletRequest request) throws Exception {

        System.out.println(memberRequestDto);
        AccessTokenDto findAccessToken = memberService.findMemberKeyByEmail(memberRequestDto.getEmail());
        MemberResponseDto findMember = memberService.findMemberByEmail(new AESCipher(findAccessToken.getEncryptKey()).encrypt(memberRequestDto.getEmail()));
        System.out.println("findMEmber: " + findMember);

        /**
         * 회원 접근 제한 체크
         */
        String verifiedMember = memberService.isVerifiedMember(memberRequestDto.getEmail(), memberRequestDto.getPassword(),
                findAccessToken.getMemberId());
        if (verifiedMember.equals(LoginConstants.NO_ACCOUNT.getValue())) {
//            throw new Exception("존재하지 않는 회원입니다.");
            throw new Exception();
        } else if (verifiedMember.equals(LoginConstants.NO_MATCH_INFO.getValue())) {
            int loginFailCount = findMember.getLoginFailCount() + 1;
            memberService.updateLoginFailCount(loginFailCount, findMember.getMemberId());
            if(loginFailCount >= 5) {
                memberService.lockMember(findMember.getMemberId(), LocalDateTime.now(), MEMBER_LOCK);
            }
            throw new Exception("로그인 정보가 일치하지 않습니다.");
        } else if (!findMember.getMemberStatus()) {
            if (findMember.getAccountStatus().equals(MEMBER_LOCK.getValue())) {
                throw new Exception("계정 잠금 회원입니다.");
            } else if (findMember.getAccountStatus().equals(MEMBER_PAUSE.getValue())) {
                throw new Exception("계정 중지 회원입니다.");
            } else throw new Exception("미승인 회원입니다.");
        }

        /**
         * 장기미접속자 날짜 체크
         */
        if (findMember.getLastLoginDate() != null) {
//            LocalDateTime compareDate = LocalDateTime.now().minusDays(90);
            LocalDateTime compareDate = LocalDateTime.now().minusDays(90);
            LocalDateTime lastLoginDate = findMember.getLastLoginDate();
            if (compareDate.isAfter(lastLoginDate)) {
                memberService.inactiveLongTermMember(findAccessToken.getMemberId(), LocalDateTime.now(), LONG_TERM_NO_LOGIN);
                System.out.println(findMember);
            }
        }
        MemberResponseDto findMemberById = memberService.findMemberById(findAccessToken.getMemberId());
        memberService.updateLastLoginDate(findMemberById.getMemberId(), LocalDateTime.now());
        memberService.resetLoginFailCount(findMemberById.getMemberId());
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(LOGIN_MEMBER, findMemberById);

        return new ResponseEntity<>(
                memberService.maskingInformationByLogIn(findMemberById.getUsername(), findMemberById.getPhoneNo()), HttpStatus.OK
        );
    }


    @PostMapping("/sign-up")
    @ResponseBody
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody(required = false) MemberRequestDto memberRequestDto) throws Exception {

        System.out.println(memberRequestDto);
        memberService.joinMember(memberRequestDto);

        return new ResponseEntity<>(
                memberService.maskingInformationBySignUp(memberRequestDto), HttpStatus.OK);
    }
}
