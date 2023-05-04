package com.example.aton_final_project.controller.user;

import com.example.aton_final_project.model.dto.FilesDto;
import com.example.aton_final_project.model.dto.InquiryRegisterRequestDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import com.example.aton_final_project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import static com.example.aton_final_project.util.session.SessionConstant.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InquiryController {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;
    private final MemberService memberService;
    private final InquiryService inquiryService;

    /**
     * 문의 신청
     * @return
     */
    @PostMapping("/inquiry-page-text")
    @ResponseBody
    public ResponseEntity<String> inquiryPageText(@RequestBody InquiryRegisterRequestDto inquiryRegisterDto,
                                              HttpServletRequest request) throws Exception {

        System.out.println("inquiry String: " + inquiryRegisterDto);
        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);
        System.out.println("<Inquiry> memberId: " + loginMember.getId());
        inquiryService.saveInquiry(inquiryRegisterDto, loginMember.getId());
        return new ResponseEntity<>(
                "text uploaded success", HttpStatus.OK
        );
    }

    @PostMapping("/inquiry-page-image")
    @ResponseBody
    public ResponseEntity<String> inquiryPageImage(HttpServletRequest request, MultipartFile[] uploadFiles) throws Exception {

        HttpSession session = request.getSession(true);
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute(LOGIN_MEMBER);

        for(MultipartFile uploadFile : uploadFiles) {
            System.out.println("content type: " + uploadFile.getContentType());

            if(!(uploadFile.getContentType().startsWith("image"))) {
                log.warn("This file is not Image.");
                throw new Exception("파일의 유형이 올바르지 않습니다.");
            }

            String originalName = uploadFile.getOriginalFilename();//파일명:모든 경로를 포함한 파일이름
            String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);
            // 날짜 폴더 생성
            String folderPath = makeFolder();
            // UUID
            String uuid = UUID.randomUUID().toString();
            // 저장할 파일 이름 중간에 "_"를 이용하여 구분
            String saveFileName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            //Paths.get() 메서드는 특정 경로의 파일 정보를 가져옵니다.(경로 정의하기)
            Path savePath = Paths.get(saveFileName);

//            // DB에 파일 저장
            inquiryService.saveInquiryFile(new FilesDto(saveFileName, fileName, savePath.toString()), inquiryService.findInquiryIdByMemberId(loginMember.getId()));

            try {
                //uploadFile에 파일을 업로드 하는 메서드 transferTo(file)
                uploadFile.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //예를 들어 getOriginalFileName()을 해서 나온 값이 /Users/Document/bootEx 이라고 한다면
            //"마지막으로온 "/"부분으로부터 +1 해준 부분부터 출력하겠습니다." 라는 뜻입니다.따라서 bootEx가 됩니다.
            log.info("fileName: " + fileName);
        }

        return new ResponseEntity<>(
                "image uploaded success", HttpStatus.OK
        );
    }

    private String makeFolder() {
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        //make folder ==================
        File uploadPathFolder = new File(uploadPath, folderPath);
        //File newFile= new File(dir,"파일명");
        //->부모 디렉토리를 파라미터로 인스턴스 생성

        if(!uploadPathFolder.exists()){
            uploadPathFolder.mkdirs();
            //만약 uploadPathFolder가 존재하지않는다면 makeDirectory하라는 의미입니다.
            //mkdir(): 디렉토리에 상위 디렉토리가 존재하지 않을경우에는 생성이 불가능한 함수
            //mkdirs(): 디렉토리의 상위 디렉토리가 존재하지 않을 경우에는 상위 디렉토리까지 모두 생성하는 함수
        }
        return folderPath;
    }

    private void printSessionInfo(HttpServletRequest request) {
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
