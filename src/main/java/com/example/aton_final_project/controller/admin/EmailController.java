package com.example.aton_final_project.controller.admin;

import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.service.file.service.FileService;
import com.example.aton_final_project.service.mail.MailService;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.util.constants.DownloadTypeConstants;
import com.example.aton_final_project.util.file.UserExcelExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.example.aton_final_project.util.constants.DownloadTypeConstants.IMAGE;
import static com.example.aton_final_project.util.constants.DownloadTypeConstants.PDF;
import static com.example.aton_final_project.util.parse.Parsing.parsingFileName;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final MailService mailService;
    private final FileService fileService;
    private final MemberService memberService;

    @PostMapping("/service-details")
    @ResponseBody
    public void send(@RequestBody MemberRequestDto memberRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {

        MemberResponseDto memberById = memberService.findMemberById(memberRequestDto.getId());
        AccessTokenDto findMemberKeyByEmail = memberService.findMemberKeyByEmail(memberById.getEmail());
        fileService.updateApprovalReason(memberRequestDto);

        UserExcelExporter excelExporter = new UserExcelExporter();

        FilesDto exportFile = excelExporter.export(response, memberById.getUsername(), findMemberKeyByEmail);

        mailService.execute(memberById, exportFile);
    }

    @GetMapping("/file-download")
    public void fileDownload(@RequestParam("serviceId") Long serviceId,
                             @RequestParam("type") DownloadTypeConstants type, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<MemberServiceRegisterResponseDto> findService = fileService.findServiceByServiceId(serviceId);
        String encodeName = "utf-8";
        try {
            String fileUrl, fileName;
            if (type.equals(PDF)) {
                int read = 0;
                System.out.println("PDF");
                fileUrl = findService.get(0).getFileUrl();
                fileName = findService.get(0).getOriginalFileName();

                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=" + URLEncoder.encode(fileName, encodeName);
                response.setHeader(headerKey, headerValue);
                response.setContentType("application/octet-stream"); //FileInputStream
                FileInputStream fileInputStream = new FileInputStream(fileUrl);
                OutputStream out = response.getOutputStream();

                byte[] buffer = new byte[1024];
                while ((read = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.close();
            } else if (type.equals(IMAGE)) {
                response.setContentType("application/octet-stream"); //FileInputStream
                String[] filesList = new String[findService.size() - 1];

                for (int i = 0; i < filesList.length; i++) {
                    filesList[i] = findService.get(i + 1).getFileUrl();
                }
                response.setContentType("application/zip");
                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=" + URLEncoder.encode(parsingFileName(findService.get(0).getUsername()), encodeName) + ".zip";
                response.setHeader(headerKey, headerValue);
                try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
                    for (String files : filesList) {
                        Path src = Paths.get(files);
                        try (FileInputStream fileInputStream = new FileInputStream(src.toFile())) {
                            ZipEntry zipEntry = new ZipEntry(src.getFileName().toString());
                            zipOutputStream.putNextEntry(zipEntry);

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fileInputStream.read(buffer)) > 0) {
                                zipOutputStream.write(buffer, 0, length);
                            }
                            zipOutputStream.closeEntry();
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new Exception("download error");
        }
    }

    @GetMapping("/guide-file-download")
    public void guideFileDownload(@RequestParam("serviceId") Long serviceId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<MemberServiceRegisterResponseDto> findService = fileService.findServiceByServiceId(serviceId);
        String encodeName = "utf-8";
        try {
            String fileUrl, fileName;
            if (findService.get(0).getServiceStatus() == 1) {
                int read = 0;
                System.out.println("GUIDE_PDF_FILE");
                fileUrl = "C:/homework/aton_final_project/src/main/resources/static/guidefile/PASS인증서_서비스_가이드문서.pdf";
                fileName = "PASS인증서_서비스_가이드문서.pdf";

                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=" + URLEncoder.encode(fileName, encodeName);
                response.setHeader(headerKey, headerValue);
                response.setContentType("application/octet-stream"); //FileInputStream
                FileInputStream fileInputStream = new FileInputStream(fileUrl);
                OutputStream out = response.getOutputStream();

                byte[] buffer = new byte[1024];
                while ((read = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.close();
            }
        } catch (Exception e) {
            throw new Exception("download error");
        }
    }
}