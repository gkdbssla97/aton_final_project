package com.example.aton_final_project.controller;

import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.service.mail.MailService;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.util.AESCipher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final MemberService memberService;
    private final MailService mailService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LogInResponseDto> loginV2(@RequestBody(required = false) MemberRequestDto memberRequestDto,
                                                    HttpServletResponse response, HttpServletRequest request) throws Exception {

        /**
         * 로그인 유효성검사
         */
        memberService.validationLoginInfo(memberRequestDto);

        AccessTokenDto findAccessToken = memberService.findMemberKeyByEmail(memberRequestDto.getEmail());
        MemberResponseDto findMember = memberService.findMemberByEmail(new AESCipher(findAccessToken.getEncryptKey()).encrypt(memberRequestDto.getEmail()));

        /**
         * 회원 접근 제한 체크
         */
        memberService.verificationMemberAccessAuthority(memberRequestDto, findAccessToken, findMember);

        /**
         * 장기미접속자 날짜 체크
         */
        memberService.confirmLongTermInactiveMember(findAccessToken, findMember);

        MemberResponseDto findMemberById = memberService.findMemberById(findAccessToken.getMemberId());
        memberService.updateLastLoginDate(findMemberById.getMemberId(), LocalDateTime.now());
        memberService.resetLoginFailCount(findMemberById.getMemberId());
        System.out.println("adminApproval: " + findMemberById);
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(LOGIN_MEMBER, findMemberById);

        log.info("User: {}", memberService.maskingInformationByLogIn(findMemberById));
        return new ResponseEntity<>(
                memberService.maskingInformationByLogIn(findMemberById), HttpStatus.OK
        );
    }

    @PostMapping("/sign-up")
    @ResponseBody
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody(required = false) MemberRequestDto memberRequestDto) throws Exception {

        /**
         * 회원가입 유효성 검사
         */
        memberService.joinMember(memberRequestDto);

        log.info("join User: {}", memberService.maskingInformationBySignUp(memberRequestDto));

        return new ResponseEntity<>(
                memberService.maskingInformationBySignUp(memberRequestDto), HttpStatus.OK);
    }

    @PostMapping("/lost-password")
    @ResponseBody
    public ResponseEntity<SignUpResponseDto> lostPassword(@RequestBody(required = false) MemberRequestDto memberRequestDto) throws Exception {

        /**
         * 비밀번호 찾기 회원 검증
         */
        AccessTokenDto memberKeyByEmail = memberService.findMemberKeyByEmail(memberRequestDto.getEmail());
        MemberResponseDto verificationMember = memberService.verificationUsernameAndEmail(memberKeyByEmail, memberRequestDto.getUsername());
        mailService.executeFindPwd(verificationMember);

        log.info("join User: {}", memberService.maskingInformationBySignUp(memberRequestDto));

        return new ResponseEntity<>(
                memberService.maskingInformationBySignUp(memberRequestDto), HttpStatus.OK);
    }
}
