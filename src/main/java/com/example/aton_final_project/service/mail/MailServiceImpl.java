package com.example.aton_final_project.service.mail;

import com.example.aton_final_project.model.dto.AttachFileDto;
import com.example.aton_final_project.model.dto.FilesDto;
import com.example.aton_final_project.model.dto.MailDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JavaMailSender javaMailSender;

    public void sendMail(MailDto mailDto, String username) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // use multipart (true)

            InternetAddress toAddress = new InternetAddress(mailDto.getReceiver(), username, "UTF-8");

            mimeMessageHelper.setSubject(MimeUtility.encodeText(mailDto.getSubject(), "UTF-8", "B")); // Base64 encoding
            mimeMessageHelper.setText(mailDto.getContent(), mailDto.isUseHtmlYn());
            mimeMessageHelper.setFrom(new InternetAddress(mailDto.getSender(), mailDto.getReceiver(), "UTF-8"));
            mimeMessageHelper.setTo(toAddress);

            if (!CollectionUtils.isEmpty(mailDto.getAttachFileList())) {
                for (AttachFileDto attachFileDto : mailDto.getAttachFileList()) {
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(attachFileDto.getRealFileNm()));
                    mimeMessageHelper.addAttachment(MimeUtility.encodeText(attachFileDto.getAttachFileNm(), "UTF-8", "B"), fileSystemResource);
                }
            }

            javaMailSender.send(mimeMessage);

            log.info("MailServiceImpl.sendMail() :: SUCCESS");
        } catch (Exception e) {
            log.info("MailServiceImpl.sendMail() :: FAILED");
            e.printStackTrace();
        }
    }

    @Override
    public void execute(MemberResponseDto member, FilesDto filesDto) throws Exception {
        try {
            String mailSubject = String.format("[%s] 님 서비스 승인 메일", member.getUsername());
            String mailContent = "<p>안녕하세요.</p><p>" + member.getUsername() + "님께서 신청하신 서비스는 [승인]되었습니다.</p>" +
                    "<p>첨부파일을 확인해주시기 바랍니다.</p><p>감사합니다.</p>";

            String toAddress = member.getEmail();
            List<AttachFileDto> atchFileList = new ArrayList<>(Arrays.asList(new AttachFileDto(filesDto.getFileUrl(), filesDto.getFilename())));

            sendMail(new MailDto("admin@atonIntern.com", toAddress, mailSubject, mailContent, true, atchFileList), member.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
