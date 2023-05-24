package com.example.aton_final_project.service;

import com.example.aton_final_project.model.dto.FilesDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import com.example.aton_final_project.service.file.service.FileService;
import com.example.aton_final_project.util.constants.DataTypeConstants;
import com.example.aton_final_project.util.error.code.CommonError;
import com.example.aton_final_project.util.error.code.ServiceRegisterError;
import com.example.aton_final_project.util.error.customexception.CommonCustomException;
import com.example.aton_final_project.util.error.customexception.ServiceCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

import static com.example.aton_final_project.util.constants.DataTypeConstants.INQUIRY;
import static com.example.aton_final_project.util.constants.DataTypeConstants.SERVICE;
import static com.example.aton_final_project.util.constants.DownloadTypeConstants.IMAGE;
import static com.example.aton_final_project.util.constants.DownloadTypeConstants.PDF;
import static com.example.aton_final_project.util.constants.ServiceConstants.IMG_FILE;
import static com.example.aton_final_project.util.constants.ServiceConstants.PDF_FILE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;
    private final FileService fileService;
    private final InquiryService inquiryService;

    @Override
    public void uploadFiles(MultipartFile[] uploadFiles, MemberResponseDto loginMember, DataTypeConstants type) {
        if (type.equals(SERVICE)) {
            if (uploadFiles == null) {
                throw new ServiceCustomException(ServiceRegisterError.MISSING_REQUIRED_ITEM, IMG_FILE.getValue());
            } else if (Arrays.stream(uploadFiles).filter(x -> x.getContentType().startsWith(IMAGE.getValue())).count() != 2) {
                throw new ServiceCustomException(ServiceRegisterError.MISSING_REQUIRED_ITEM, IMG_FILE.getValue());
            } else if (Arrays.stream(uploadFiles).filter(x -> x.getContentType().endsWith(PDF.getValue())).count() != 1) {
                throw new ServiceCustomException(ServiceRegisterError.MISSING_REQUIRED_ITEM, PDF_FILE.getValue());
            }
        }

        for (MultipartFile uploadFile : uploadFiles) {
            if (type.equals(SERVICE)) {
                fileService.confirmUploadedFileDataType(uploadFile);
            } else if (type.equals(INQUIRY)) {
                inquiryService.confirmUploadedFileDataType(uploadFile);
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

            // DB에 파일 저장
            if (type.equals(SERVICE)) {
                Long serviceIdByMemberId = fileService.findServiceIdByMemberId(loginMember.getMemberId());
                System.out.println("serviceId: " + serviceIdByMemberId);
                fileService.saveFile(new FilesDto(saveFileName, fileName, savePath.toString()), fileService.findServiceIdByMemberId(loginMember.getMemberId()));
            } else if (type.equals(INQUIRY)) {
                Long inquiryIdByMemberId = inquiryService.findInquiryIdByMemberId(loginMember.getMemberId());
                System.out.println("inquiryId: " + inquiryIdByMemberId);
                inquiryService.saveInquiryFile(new FilesDto(saveFileName, fileName, savePath.toString()), inquiryService.findInquiryIdByMemberId(loginMember.getMemberId()));
            }

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
    }

    private String makeFolder() {
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        //make folder ==================
        File uploadPathFolder = new File(uploadPath, folderPath);
        //File newFile= new File(dir,"파일명");
        //->부모 디렉토리를 파라미터로 인스턴스 생성

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
            //만약 uploadPathFolder가 존재하지 않는다면 makeDirectory
            //mkdir(): 디렉토리에 상위 디렉토리가 존재하지 않을경우에는 생성이 불가능한 함수
            //mkdirs(): 디렉토리의 상위 디렉토리가 존재하지 않을 경우에는 상위 디렉토리까지 모두 생성하는 함수
        }
        return folderPath;
    }

    public String verificationAuthority() {
        try {
            throw new CommonCustomException(CommonError.NO_PERMISSION);
        } catch (CommonCustomException e) {
            return "redirect:/main";
        }
    }
}
