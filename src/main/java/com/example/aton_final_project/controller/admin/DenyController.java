package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.service.file.FileService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class DenyController {

    private final FileService fileService;
    private final InquiryService inquiryService;

    /**
     * 반려 사유 제출
     * @return
     */
    @PostMapping("/service-deny-page")
    @ResponseBody
    public void adminServiceDenyPage(@RequestBody MemberServiceRegisterResponseDto memberServiceRegisterDto, HttpServletRequest request, Model model) throws Exception {
        fileService.updateDenyReason(memberServiceRegisterDto);
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
