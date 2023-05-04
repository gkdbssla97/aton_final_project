package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.service.file.FileService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

import static com.example.aton_final_project.util.constants.DownloadTypeConstants.IMG;
import static com.example.aton_final_project.util.constants.DownloadTypeConstants.PDF;
import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminMainController {

    private final FileService fileService;
    private final InquiryService inquiryService;

    /**
     * 메인 페이지(관리자_대시보드)
     *
     * @return
     */
    @GetMapping("/main")
    public String adminMainPage(@SessionAttribute(name = LOGIN_MEMBER, required = false) MemberResponseDto loginMember,
                           HttpServletRequest request, Model model) {
        System.out.println("login: " + loginMember);
        if (loginMember == null) {
            return "redirect:/login";
        }
        printSessionInfo(request);
        model.addAttribute("member", loginMember);
        return "pages/admin-dashboard";
    }

    /**
     * 전체 회원 서비스 신청 내역
     * @return
     */
    @GetMapping("/service-details-page")
    public String adminServiceListPage(HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
        List<MemberServiceRegisterResponseDto> serviceRegisterList = fileService.findAllServiceRegister();
        System.out.println("size; " + serviceRegisterList.size());
        model.addAttribute("member", loginMember);
        model.addAttribute("serviceRegisterList", serviceRegisterList);

        return "pages/admin-service-details";
    }

    /**
     *  회원 서비스 상세 정보
     * @return
     */
    @GetMapping("/member-service-page")
    public String adminMemberServiceDetailPage(@RequestParam("serviceId") Long serviceId, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        System.out.println("ServiceId: " + serviceId);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        List<MemberServiceRegisterResponseDto> findService = fileService.findServiceByServiceId(serviceId);
        model.addAttribute("member", loginMember);
        model.addAttribute("service", findService);
        model.addAttribute("IMG", IMG);
        model.addAttribute("PDF", PDF);
        System.out.println(findService);
        String fileUrl = findService.get(1).getFileUrl();
        System.out.println(fileUrl.split("uploaded_files")[1]);

        return "pages/admin-service-page";
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
