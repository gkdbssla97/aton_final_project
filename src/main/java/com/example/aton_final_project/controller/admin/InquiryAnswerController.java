package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.InquiryRegisterRequestDto;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
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
public class InquiryAnswerController {

    private final InquiryService inquiryService;

    /**
     * 문의 답변 제출
     * @return
     */
    @PostMapping("/answer-inquiry")
    @ResponseBody
    public void adminInquiryAnswerPage(@RequestBody InquiryRegisterRequestDto inquiryRegisterRequestDto, HttpServletRequest request, Model model) throws Exception {

        inquiryService.updateInquiryAnswer(inquiryRegisterRequestDto);
    }
}
