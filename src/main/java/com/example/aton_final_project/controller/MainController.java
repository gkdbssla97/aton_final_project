package com.example.aton_final_project.controller;

import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.service.file.FileService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final FileService fileService;
    private final InquiryService inquiryService;

    /**
     * 메인 페이지(대시보드)
     *
     * @return
     */
    @GetMapping("/main")
    public String mainPage(@SessionAttribute(name = LOGIN_MEMBER, required = false) MemberResponseDto loginMember,
                           HttpServletRequest request, Model model) {
        System.out.println("login: " + loginMember);
        if (loginMember == null) {
            return "redirect:/login";
        }
        printSessionInfo(request);
        model.addAttribute("member", loginMember);
        return "pages/dashboard";
    }

    /**
     * 서비스 신청
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
     * @return
     */
    @GetMapping("/service-details-page")
    public String serviceDetailPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
        List<MemberServiceRegisterResponseDto> serviceRegisterList = fileService.findServiceRegisterById(loginMember.getId());
        System.out.println("size; " + serviceRegisterList.size());
        model.addAttribute("member", loginMember);
        model.addAttribute("serviceRegisterList", serviceRegisterList);

        return "pages/service-details";
    }

    /**
     * 문의하기
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
     * @return
     */
    @GetMapping("/inquiry-details-page")
    public String inquiryDetailPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
        List<InquiryRegisterResponseDto> inquiryList = inquiryService.findInquiriesRegisterById(loginMember.getId());
        System.out.println("size; " + inquiryList.size());
        model.addAttribute("member", loginMember);
        model.addAttribute("inquiryList", inquiryList);

        return "pages/inquiry-details";
    }

    /**
     * 프로필
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
