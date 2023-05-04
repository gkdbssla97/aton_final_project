package com.example.aton_final_project.controller;

import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.service.member.MemberService;
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
        System.out.println(findAccessToken);

        if (!memberService.isVerifiedMember(memberRequestDto.getEmail(), memberRequestDto.getPassword(),
                findAccessToken.getMemberId())) {
            throw new Exception("존재하지 않는 회원입니다.");
        }

        MemberResponseDto findMemberById = memberService.findMemberById(findAccessToken.getMemberId());

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
        memberService.joinAdmin(memberRequestDto);

        return new ResponseEntity<>(
                memberService.maskingInformationBySignUp(memberRequestDto), HttpStatus.OK);
    }
}
