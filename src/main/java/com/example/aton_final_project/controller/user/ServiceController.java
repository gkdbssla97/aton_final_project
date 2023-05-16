package com.example.aton_final_project.controller.user;

import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterRequestDto;
import com.example.aton_final_project.service.CommonService;
import com.example.aton_final_project.service.file.service.FileService;
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

import static com.example.aton_final_project.util.constants.DataTypeConstants.SERVICE;
import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ServiceController {

    private final CommonService commonService;
    private final FileService fileService;

    /**
     * 서비스 신청
     * @return
     */
    @PostMapping("/service-page-text")
    @ResponseBody
    public ResponseEntity<String> servicePageText(@RequestBody MemberServiceRegisterRequestDto memberServiceRegisterDto,
                                              HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        fileService.saveServiceRegister(memberServiceRegisterDto, loginMember.getMemberId());
        return new ResponseEntity<>(
                "text uploaded success", HttpStatus.OK
        );
    }

    @PostMapping("/service-page-image")
    @ResponseBody
    public ResponseEntity<String> servicePageImage(HttpServletRequest request, MultipartFile[] uploadFiles) throws Exception {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        commonService.uploadFiles(uploadFiles, loginMember, SERVICE);

        return new ResponseEntity<>(
                "image uploaded success", HttpStatus.OK
        );
    }
}
