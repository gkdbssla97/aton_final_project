package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.service.CommonService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import com.example.aton_final_project.service.file.service.FileService;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

import static com.example.aton_final_project.util.constants.AuthoritiesConstants.ROLE_MEMBER;
import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminDashboardController {
    private final CommonService commonService;
    private final MemberService memberService;
    private final FileService fileService;
    private final InquiryService inquiryService;
    private final StatisticsService statisticsService;

    /**
     * 메인 페이지(관리자_대시보드)
     *
     * @return
     */
    @GetMapping("/main")
    public String adminMainPage(@SessionAttribute(name = LOGIN_MEMBER, required = false) MemberResponseDto loginMember,
                                HttpServletRequest request, Model model) throws Exception {
        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        printSessionInfo(request);
        model.addAttribute("member", loginMember);

        /**
         * 회원 관련 통계 수치
         */
        model.addAttribute("countAllMember", memberService.countAllMember());
        model.addAttribute("countTodayMember", memberService.countTodayMember());
        model.addAttribute("countMemberGrowth", memberService.countMemberGrowth().parsing_member());
        model.addAttribute("countLoginGrowth", memberService.countMemberLogin().parsing_login());

        /**
         * 서비스 관련 통계 수치
         */
        model.addAttribute("countAllService", fileService.countAllService());
        model.addAttribute("countServiceGrowth", fileService.countServiceRequest().parsing_service());
        model.addAttribute("confirmService", fileService.findLastServiceRegister(loginMember.getMemberId()).get(0));

        /**
         * 문의 관련 통계 수치
         */
        model.addAttribute("countAllInquiry", inquiryService.countAllInquiry());
        model.addAttribute("countInquiryGrowth", inquiryService.countInquiryRequest().parsing_inquiry());
        model.addAttribute("confirmInquiry", inquiryService.findLastInquiry(loginMember.getMemberId()).get(0));

        /**
         * 전체 내역 통계 수치
         */
        model.addAttribute("findAdmin", statisticsService.findAdminApprovalChangeRate());
        model.addAttribute("findMember", statisticsService.findMemberApprovalChangeRate());
        model.addAttribute("findService", statisticsService.findServiceApprovalChangeRate());
        model.addAttribute("findInquiry", statisticsService.findInquiryApprovalChangeRate());

        /**
         * 최신 신청/등록 현황
         */
        model.addAttribute("lastOneMember", statisticsService.findLastOneMember());
        model.addAttribute("lastOneApprovedMember", statisticsService.findLastOneApprovedMember());
        model.addAttribute("lastOneService", statisticsService.findLastOneService());
        model.addAttribute("lastOneInquiry", statisticsService.findLastOneMember());

        return "pages/admin-dashboard";
    }

    private void printSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        MemberResponseDto memberResponseDto = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        log.info("login SESSION INFO: " + memberResponseDto);
        log.info("sessionId={}", session.getId());
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());
    }
}
