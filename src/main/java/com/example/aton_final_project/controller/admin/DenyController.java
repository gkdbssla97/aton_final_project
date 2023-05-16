package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import com.example.aton_final_project.service.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class DenyController {

    private final FileService fileService;

    /**
     * 반려 사유 제출
     * @return
     */
    @PostMapping("/service-deny-page")
    @ResponseBody
    public void adminServiceDenyPage(@RequestBody MemberServiceRegisterResponseDto memberServiceRegisterDto, HttpServletRequest request, Model model) throws Exception {
        fileService.updateDenyReason(memberServiceRegisterDto);
    }
}
