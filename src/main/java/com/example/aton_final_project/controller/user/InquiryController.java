package com.example.aton_final_project.controller.user;

import com.example.aton_final_project.model.dto.InquiryRegisterRequestDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.service.CommonService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.example.aton_final_project.util.constants.DataTypeConstants.INQUIRY;
import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InquiryController {

    private final CommonService commonService;
    private final InquiryService inquiryService;

    /**
     * 문의 신청
     * @return
     */
    @PostMapping("/inquiry-page-text")
    @ResponseBody
    public ResponseEntity<String> inquiryPageText(@RequestBody InquiryRegisterRequestDto inquiryRegisterDto,
                                                  HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        inquiryService.saveInquiry(inquiryRegisterDto, loginMember.getMemberId());
        return new ResponseEntity<>(
                "text uploaded success", HttpStatus.OK
        );
    }

    @PostMapping("/inquiry-page-image")
    @ResponseBody
    public ResponseEntity<String> inquiryPageImage(HttpServletRequest request, MultipartFile[] uploadFiles) throws Exception {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);
        commonService.uploadFiles(uploadFiles, loginMember, INQUIRY);

        return new ResponseEntity<>(
                "inquiry uploaded success", HttpStatus.OK
        );
    }
}
