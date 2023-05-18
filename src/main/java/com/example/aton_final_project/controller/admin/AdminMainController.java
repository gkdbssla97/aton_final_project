package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.service.BoardService;
import com.example.aton_final_project.service.CommonService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import com.example.aton_final_project.service.file.service.FileService;
import com.example.aton_final_project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.example.aton_final_project.util.constants.AuthoritiesConstants.ROLE_MEMBER;
import static com.example.aton_final_project.util.constants.DataTypeConstants.*;
import static com.example.aton_final_project.util.constants.DownloadTypeConstants.IMAGE;
import static com.example.aton_final_project.util.constants.DownloadTypeConstants.PDF;
import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminMainController {

    private final CommonService commonService;
    private final MemberService memberService;
    private final FileService fileService;
    private final InquiryService inquiryService;

    @Autowired
    private BoardService boardService;

//    /**
//     * 메인 페이지(관리자_대시보드)
//     *
//     * @return
//     */
//    @GetMapping("/main")
//    public String adminMainPage(@SessionAttribute(name = LOGIN_MEMBER, required = false) MemberResponseDto loginMember,
//                                HttpServletRequest request, Model model) {
//        /**
//         * 권한 검증에 따른 접근 제어
//         */
//        if (loginMember == null) {
//            return "redirect:/login";
//        }
//        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
//            return commonService.verificationAuthority();
//        }
//
//        printSessionInfo(request);
//        model.addAttribute("member", loginMember);
//
//        /**
//         * 회원 관련 통계 수치
//         */
//        model.addAttribute("countAllMember", memberService.countAllMember());
//        model.addAttribute("countTodayMember", memberService.countTodayMember());
//        model.addAttribute("countMemberGrowth", memberService.countMemberGrowth().parsing_member());
//        model.addAttribute("countLoginGrowth", memberService.countMemberLogin().parsing_login());
//
//        /**
//         * 서비스 관련 통계 수치
//         */
//        model.addAttribute("countAllService", fileService.countAllService());
//        model.addAttribute("countServiceGrowth", fileService.countServiceRequest().parsing_service());
//        model.addAttribute("confirmService", fileService.findLastServiceRegister(loginMember.getMemberId()).get(0));
//
//        /**
//         * 문의 관련 통계 수치
//         */
//        model.addAttribute("countAllInquiry", inquiryService.countAllInquiry());
//        model.addAttribute("countInquiryGrowth", inquiryService.countInquiryRequest().parsing_inquiry());
//        model.addAttribute("confirmInquiry", inquiryService.findLastInquiry(loginMember.getMemberId()).get(0));
//
//        return "pages/admin-dashboard";
//    }

    /**
     * 전체 관리자 추가내역
     *
     * @return
     */
    @GetMapping("/admin-details-page")
    public String adminRegisterListPage(HttpServletRequest request, Model model,
                                        @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                        @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        model.addAttribute("member", loginMember);
        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, ADMIN_REGISTER, 0L);
        model.addAttribute("memberList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/admin-register-details";
    }

    /**
     * 관리자 상세 정보
     *
     * @return
     */
    @GetMapping("/member-admin-page")
    public String adminRegisterDetailPage(@RequestParam("memberId") Long memberId, HttpServletRequest request, Model model,
                                          @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                          @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        MemberResponseDto findMemberById = memberService.findMemberById(memberId);
        model.addAttribute("member", loginMember);
        model.addAttribute("memberInfo", findMemberById);

        return "pages/admin-register-page";
    }

    /**
     * 전체 회원 가입 신청내역
     *
     * @return
     */
    @GetMapping("/join-details-page")
    public String adminJoinListPage(HttpServletRequest request, Model model,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                    @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        model.addAttribute("member", loginMember);
        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, MEMBER_APPROVAL, 0L);
        model.addAttribute("memberList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/admin-join-details";
    }

    /**
     * 회원 가입 상세 정보
     *
     * @return
     */
    @GetMapping("/member-join-page")
    public String adminMemberJoinDetailPage(@RequestParam("memberId") Long memberId, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        MemberResponseDto findMemberById = memberService.findMemberById(memberId);
        model.addAttribute("member", loginMember);
        model.addAttribute("memberInfo", findMemberById);

        return "pages/admin-join-page";
    }

    @GetMapping("/service-details-page")
    public String adminServiceListPageV2(HttpServletRequest request, Model model,
                                         @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                         @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                         @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        List<MemberServiceRegisterResponseDto> serviceRegisterList = fileService.findAllServiceRegister();

        model.addAttribute("member", loginMember);

        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, SERVICE, 0L);
        model.addAttribute("serviceRegisterList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/admin-service-details";
    }

    /**
     * 회원 서비스 상세 정보
     *
     * @return
     */
    @GetMapping("/member-service-page")
    public String adminMemberServiceDetailPage(@RequestParam("serviceId") Long serviceId, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        List<MemberServiceRegisterResponseDto> findService = fileService.findServiceByServiceId(serviceId);
        model.addAttribute("member", loginMember);
        model.addAttribute("service", findService);
        model.addAttribute("IMG", IMAGE);
        model.addAttribute("PDF", PDF);

        return "pages/admin-service-page";
    }

    /**
     * 전체 회원 문의내역
     *
     * @return
     */
    @GetMapping("/inquiry-details-page")
    public String adminInquiryListPageV2(HttpServletRequest request, Model model,
                                         @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                         @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
                                         @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        model.addAttribute("member", loginMember);
        Map<String, Object> map = boardService.boardList(currentPage, searchType, keyword, INQUIRY, 0L);
        model.addAttribute("inquiryList", map.get("list"));
        model.addAttribute("currentPage", map.get("currentPage"));
        model.addAttribute("lastPage", map.get("lastPage"));
        model.addAttribute("startPageNum", map.get("startPageNum"));
        model.addAttribute("lastPageNum", map.get("lastPageNum"));

        return "pages/admin-inquiry-details";
    }

    /**
     * 회원 문의 상세 정보
     *
     * @return
     */
    @GetMapping("/member-inquiry-page")
    public String adminMemberInquiryDetailPage(@RequestParam("inquiryId") Long inquiryId, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        /**
         * 권한 검증에 따른 접근 제어
         */
        if (loginMember == null) {
            return "redirect:/login";
        }
        if (loginMember.getAuthority().equals(ROLE_MEMBER.getValue())) {
            return commonService.verificationAuthority();
        }

        List<InquiryRegisterResponseDto> findInquiry = inquiryService.findInquiryByInquiryId(inquiryId);
        model.addAttribute("member", loginMember);
        model.addAttribute("inquiry", findInquiry);

        return "pages/admin-inquiry-page";
    }
}
