package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.MemberAuthorityDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.util.error.code.AuthenticationRegistrationError;
import com.example.aton_final_project.util.error.customexception.RegisterCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.example.aton_final_project.util.constants.AccountStatus.*;
import static com.example.aton_final_project.util.constants.LoginConstants.NEW_PASSWORD;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminMemberController {

    private final MemberService memberService;

    /**
     * 가입 승인
     *
     * @return
     */
    @PostMapping("/approval-member")
    @ResponseBody
    public ResponseEntity<String> adminApprovalMemberJoinPage(@RequestBody MemberResponseDto memberResponseDto, HttpServletRequest request, Model model) throws Exception {

        memberService.updateMemberApproval(memberResponseDto.getMemberId());

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 관리자 등록
     *
     * @return
     */
    @PostMapping("/register-member-to-admin")
    @ResponseBody
    public ResponseEntity<String> adminRegisterMemberToAdminPage(@RequestBody MemberResponseDto memberResponseDto, HttpServletRequest request, Model model) throws Exception {

        memberService.updateMemberToAdmin(memberResponseDto.getMemberId());

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 계정 권한 설정
     *
     * @return
     */
    @PostMapping("/member-authority-setting")
    @ResponseBody
    public ResponseEntity<String> adminEditMemberAuthorityPage(@RequestBody MemberAuthorityDto memberAuthorityDto,
                                                               HttpServletRequest request, Model model) throws Exception {

        if (memberAuthorityDto.getAccountStatus().equals(LONG_TERM_NO_LOGIN.getValue())) {
            System.out.println("장기미접속자 해제");
            memberService.activeLongTermMember(memberAuthorityDto.getMemberId(), NORMAL);
            return new ResponseEntity<>(LONG_TERM_NO_LOGIN.getValue(), HttpStatus.OK);
        } else if (memberAuthorityDto.getAccountStatus().equals(MEMBER_LOCK.getValue())) {
            System.out.println("계정 잠금 해제");
            memberService.activeLongTermMember(memberAuthorityDto.getMemberId(), NORMAL);
            return new ResponseEntity<>(MEMBER_LOCK.getValue(), HttpStatus.OK);
        } else if (memberAuthorityDto.getAccountStatus().equals(MEMBER_PAUSE.getValue())) {
            System.out.println("계정 중지");
            memberService.pauseMember(memberAuthorityDto.getMemberId(), LocalDateTime.now(), MEMBER_PAUSE);
            return new ResponseEntity<>(MEMBER_PAUSE.getValue(), HttpStatus.OK);
        } else if (memberAuthorityDto.getAccountStatus().equals(MEMBER_SECESSION.getValue())) {
            System.out.println("회원 탈퇴");
            memberService.deleteMember(memberAuthorityDto.getMemberId());
            return new ResponseEntity<>(MEMBER_SECESSION.getValue(), HttpStatus.OK);
        }
        throw new Exception();
    }

    /**
     * 관리자 페이지 회원 비밀번호 변경
     *
     * @return
     */
    @PostMapping("/change-password-setting")
    @ResponseBody
    public ResponseEntity<String> adminChangeMemberPassword(@RequestBody MemberResponseDto memberResponseDto, HttpServletRequest request, Model model) throws Exception {
        if (memberResponseDto.getNewPassword().equals("") || memberResponseDto.get_newPassword().equals("")) {
            throw new RegisterCustomException(AuthenticationRegistrationError.MISSING_REQUIRED_ITEM, NEW_PASSWORD.getValue());
        }
        memberService.editMemberInformation(memberResponseDto.getMemberId(), memberResponseDto.getNewPassword());

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
