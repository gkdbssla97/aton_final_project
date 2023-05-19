package com.example.aton_final_project.controller;

import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.service.BoardService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import com.example.aton_final_project.service.file.service.FileService;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.aton_final_project.util.constants.DataTypeConstants.INQUIRY;
import static com.example.aton_final_project.util.constants.DataTypeConstants.SERVICE;
import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final FileService fileService;
    private final InquiryService inquiryService;
    private final MemberService memberService;
    private final StatisticsService statisticsService;
    @Autowired
    private BoardService boardService;

    /**
     * 메인 페이지(대시보드)
     *
     * @return
     */
    @GetMapping("/main")
    public String mainPage(@SessionAttribute(name = LOGIN_MEMBER, required = false) MemberResponseDto loginMember,
                           HttpServletRequest request, Model model) {
        log.info("login={} ", loginMember);
        if (loginMember == null) {
            return "redirect:/login";
        }
        printSessionInfo(request);
        model.addAttribute("member", loginMember);

        /**
         * 회원 관련 통계 수치
         */
        model.addAttribute("countAllMember", memberService.countAllMember());
        model.addAttribute("countTodayMember", memberService.countTodayMember());
        model.addAttribute("countMemberGrowth", memberService.countMemberGrowth());
        model.addAttribute("countLoginGrowth", memberService.countMemberLogin());

        /**
         * 내 서비스 관련 통계 수치
         */
        model.addAttribute("findMyService", statisticsService.findMyService(loginMember.getMemberId()));
        model.addAttribute("countAllService", fileService.countAllService());
        model.addAttribute("confirmService", fileService.findLastServiceRegister(loginMember.getMemberId()).get(0));

        /**
         * 내 문의 관련 통계 수치
         */
        model.addAttribute("findMyInquiry", statisticsService.findMyInquiry(loginMember.getMemberId()));
        model.addAttribute("countAllInquiry", inquiryService.countAllInquiry());
        model.addAttribute("confirmInquiry", inquiryService.findLastInquiry(loginMember.getMemberId()).get(0));

        return "pages/dashboard";
    }

    /**
     * 서비스 신청
     *
     * @return
     */
    @GetMapping("/service-page")
    public String servicePage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", loginMember);

        return "pages/service-page";
    }

    /**
     * 서비스 신청 내역
     *
     * @return
     */
    @GetMapping("/service-details-page")
    public String serviceDetailPage(HttpServletRequest request, Model model,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                    @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }

        List<MemberServiceRegisterResponseDto> serviceRegisterList = fileService.findServiceRegisterById(loginMember.getMemberId());
//        System.out.println("size; " + serviceRegisterList.size());
        model.addAttribute("member", loginMember);

        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, SERVICE, loginMember.getMemberId());
        model.addAttribute("totalList", map.get("totalList"));
        model.addAttribute("serviceRegisterList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/service-details";
    }

    /**
     * 서비스 신청 상세 정보
     *
     * @return
     */
    @GetMapping("/answer-service-page")
    public String memberAnswerServicePage(@RequestParam("serviceId") Long serviceId, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }

        List<MemberServiceRegisterResponseDto> findService = fileService.findServiceByServiceId(serviceId);
        model.addAttribute("member", loginMember);
        model.addAttribute("serviceList", findService);

        return "pages/answer-service-page";
    }

    /**
     * 문의하기
     *
     * @return
     */
    @GetMapping("/inquiry-page")
    public String inquiryPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", loginMember);

        return "pages/inquiry-page";
    }

    /**
     * 문의 내역
     *
     * @return
     */
    @GetMapping("/inquiry-details-page")
    public String inquiryDetailPage(HttpServletRequest request, Model model,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                    @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("member", loginMember);

        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, INQUIRY, loginMember.getMemberId());
        model.addAttribute("totalList", map.get("totalList"));
        model.addAttribute("inquiryList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/inquiry-details";
    }

    /**
     * 문의 답변 상세 정보
     *
     * @return
     */
    @GetMapping("/answer-inquiry-page")
    public String memberAnswerInquiryPage(@RequestParam("inquiryId") Long inquiryId, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }

        List<InquiryRegisterResponseDto> findInquiry = inquiryService.findInquiryByInquiryId(inquiryId);
        model.addAttribute("member", loginMember);
        model.addAttribute("inquiry", findInquiry);

        return "pages/answer-inquiry-page";
    }

    /**
     * 프로필
     *
     * @return
     */
    @GetMapping("/profile")
    public String profilePage(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", loginMember);
        return "pages/profile";
    }

    /**
     * 프로필
     *
     * @return
     */
    @GetMapping("/edit-profile")
    public String editProfile(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", loginMember);
        return "pages/edit-profile";
    }

    /**
     * 로그아웃
     *
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "login";
    }

    private void printSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        MemberResponseDto memberResponseDto = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);
        log.info("login SESSION INFO={} ", memberResponseDto);
        log.info("sessionId={}", session.getId());
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());
    }
}
