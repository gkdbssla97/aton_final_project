package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.service.BoardService;
import com.example.aton_final_project.service.file.FileService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import com.example.aton_final_project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Map;

import static com.example.aton_final_project.util.constants.DataTypeConstants.*;
import static com.example.aton_final_project.util.constants.DownloadTypeConstants.IMG;
import static com.example.aton_final_project.util.constants.DownloadTypeConstants.PDF;
import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminMainController {

    private final MemberService memberService;
    private final FileService fileService;
    private final InquiryService inquiryService;

    @Autowired
    private BoardService boardService;

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
     * 전체 관리자 추가내역
     * @return
     */
    @GetMapping("/admin-details-page")
    public String adminRegisterListPage(HttpServletRequest request, Model model,
                                        @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                        @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
//        List<MemberResponseDto> memberList = memberService.findAllMember();

        model.addAttribute("member", loginMember);
        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, ADMIN_REGISTER);
        model.addAttribute("memberList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/admin-register-details";
    }

    /**
     *  관리자 상세 정보
     * @return
     */
    @GetMapping("/member-admin-page")
    public String adminRegisterDetailPage(@RequestParam("memberId") Long memberId, HttpServletRequest request, Model model,
                                          @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                          @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        System.out.println("memberId: " + memberId);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        MemberResponseDto findMemberById = memberService.findMemberById(memberId);
        System.out.println("member-admin-page: " + findMemberById);
        model.addAttribute("member", loginMember);
        model.addAttribute("memberInfo", findMemberById);
        System.out.println(findMemberById);

        return "pages/admin-register-page";
    }

    /**
     * 전체 회원 가입 신청내역
     * @return
     */
    @GetMapping("/join-details-page")
    public String adminJoinListPage(HttpServletRequest request, Model model,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                    @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login";
        }
//        List<MemberResponseDto> memberList = memberService.findAllMember();
        model.addAttribute("member", loginMember);
        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, MEMBER_APPROVAL);
        model.addAttribute("memberList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/admin-join-details";
    }

    /**
     *  회원 가입 상세 정보
     * @return
     */
    @GetMapping("/member-join-page")
    public String adminMemberJoinDetailPage(@RequestParam("memberId") Long memberId, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        System.out.println("memberId: " + memberId);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        MemberResponseDto findMemberById = memberService.findMemberById(memberId);
        model.addAttribute("member", loginMember);
        model.addAttribute("memberInfo", findMemberById);
        System.out.println(findMemberById);

        return "pages/admin-join-page";
    }


//    @GetMapping("/service-details-page")
//    public String adminServiceListPageV1(HttpServletRequest request, Model model) throws Exception {
//        HttpSession session = request.getSession(true);
//        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);
//
//        if (loginMember == null) {
//            return "redirect:/login";
//        }
//        List<MemberServiceRegisterResponseDto> serviceRegisterList = fileService.findAllServiceRegister();
//        System.out.println("size; " + serviceRegisterList.size());
//        model.addAttribute("member", loginMember);
//        model.addAttribute("serviceRegisterList", serviceRegisterList);
//
//        return "pages/admin-service-details";
//    }

    @GetMapping("/service-details-page")
    public String adminServiceListPageV2(HttpServletRequest request, Model model,
                                         @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                         @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                         @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        System.out.println("CP, ST, KW: " + currentPage + " " + searchType + " " + keyword);

        if (loginMember == null) {
            return "redirect:/login";
        }
        List<MemberServiceRegisterResponseDto> serviceRegisterList = fileService.findAllServiceRegister();
        System.out.println("size; " + serviceRegisterList.size());
        model.addAttribute("member", loginMember);
//        model.addAttribute("serviceRegisterList", serviceRegisterList);

        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, SERVICE);
        model.addAttribute("serviceRegisterList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

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

    /**
     * 전체 회원 문의내역
     * @return
     */
//    @GetMapping("/inquiry-details-page")
//    public String adminInquiryListPage(HttpServletRequest request, Model model) throws Exception {
//        HttpSession session = request.getSession(true);
//        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);
//
//        if (loginMember == null) {
//            return "redirect:/login";
//        }
//        List<InquiryRegisterResponseDto> inquiryList = inquiryService.findAllInquiry();
//        System.out.println(inquiryList);
//        System.out.println("size; " + inquiryList.size());
//        model.addAttribute("member", loginMember);
//        model.addAttribute("inquiryList", inquiryList);
//
//        return "pages/admin-inquiry-details";
//    }

    @GetMapping("/inquiry-details-page")
    public String adminInquiryListPageV2(HttpServletRequest request, Model model,
                                         @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                         @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                         @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        System.out.println("CP, ST, KW: " + currentPage + " " + searchType + " " + keyword);

        if (loginMember == null) {
            return "redirect:/login";
        }
        List<InquiryRegisterResponseDto> inquiryList = inquiryService.findAllInquiry();
        System.out.println(inquiryList);
        System.out.println("size; " + inquiryList.size());
        model.addAttribute("member", loginMember);
        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, INQUIRY);
        model.addAttribute("inquiryList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/admin-inquiry-details";
    }

    /**
     *  회원 문의 상세 정보
     * @return
     */
    @GetMapping("/member-inquiry-page")
    public String adminMemberInquiryDetailPage(@RequestParam("inquiryId") Long inquiryId, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        System.out.println("InquiryId: " + inquiryId);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        List<InquiryRegisterResponseDto> findInquiry = inquiryService.findInquiryByInquiryId(inquiryId);
        model.addAttribute("member", loginMember);
        model.addAttribute("inquiry", findInquiry);
        System.out.println(findInquiry);
        String fileUrl = findInquiry.get(0).getFileUrl();

        return "pages/admin-inquiry-page";
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
