package com.example.aton_final_project.controller.user;

import com.example.aton_final_project.model.dto.EditProfileDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.util.constants.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final MemberService memberService;

    /**
     * 회원정보 수정
     * @return
     */
    @PostMapping("/edit-profile")
    @ResponseBody
    public ResponseEntity<MemberResponseDto> profilePage(@RequestBody(required = false) EditProfileDto editProfileDto,
                                                        HttpServletResponse response, HttpServletRequest request) throws Exception {

        System.out.println(editProfileDto);

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        MemberResponseDto memberById = memberService.findMemberById(loginMember.getMemberId());
        if (!memberById.getPassword().equals(editProfileDto.getOldPassword())) {
            throw new Exception("사용자의 비밀번호가 일치하지 않습니다..");
        }


        memberService.editMemberInformation(memberById.getMemberId(), editProfileDto.getNewPassword());
        MemberResponseDto memberByIdAfter = memberService.findMemberById(loginMember.getMemberId());

        System.out.println("-> " + memberByIdAfter.getAccountStatus());
        if(memberByIdAfter.getAccountStatus().equals(AccountStatus.LONG_TERM_NO_LOGIN.getValue())) {
            System.out.println(memberByIdAfter.getUsername() + " 은 장기미접속자.");
            memberService.activeLongTermMember(memberById.getMemberId(), AccountStatus.NORMAL);
        }
        return new ResponseEntity<>(
                memberService.maskingInformationByEdit(memberByIdAfter), HttpStatus.OK
        );
    }

    public void printSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        MemberResponseDto memberResponseDto = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);
        System.out.println("login SESSION INFO: " + memberResponseDto);
        log.info("sessionId={}", session.getId());
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());
    }
}
