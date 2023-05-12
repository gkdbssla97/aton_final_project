package com.example.aton_final_project.service.member;

import com.example.aton_final_project.model.dao.MemberMapper;
import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.util.AESCipher;
import com.example.aton_final_project.util.constants.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.aton_final_project.util.constants.AccessConstants.ACCESS_TOKEN;
import static com.example.aton_final_project.util.constants.AccessConstants.ENCRYPT_KEY;
import static com.example.aton_final_project.util.constants.AdminConstant.SUPER_ADMIN_EMAIL;
import static com.example.aton_final_project.util.constants.AdminConstant.SUPER_ADMIN_USERNAME;
import static com.example.aton_final_project.util.constants.AuthoritiesConstants.ROLE_ADMIN;
import static com.example.aton_final_project.util.constants.AuthoritiesConstants.ROLE_MEMBER;
import static com.example.aton_final_project.util.constants.LoginConstants.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    public void joinAdmin(MemberRequestDto memberRequestDto) throws Exception {

        String accessToken = initializeAccessKey(ACCESS_TOKEN.getValue());
        String encryptKey = initializeAccessKey(ENCRYPT_KEY.getValue());

        MemberRequestDto encryptMember = encryptMemberInfo(memberRequestDto, encryptKey);
        memberMapper.joinAdmin(encryptMember);

        Long memberId = encryptMember.getId();
        saveMemberAccessKey(memberId, accessToken, encryptKey, memberRequestDto.getEmail());
        authorizeAdmin(memberId);
    }

    @Override
    public void authorizeAdmin(Long memberId) {
        memberMapper.authorizeAdmin(memberId);
    }

    @Override
    public void joinMember(MemberRequestDto memberRequestDto) throws Exception {
        String accessToken = initializeAccessKey(ACCESS_TOKEN.getValue());
        String encryptKey = initializeAccessKey(ENCRYPT_KEY.getValue());

        MemberRequestDto encryptMember = encryptMemberInfo(memberRequestDto, encryptKey);
        memberMapper.joinMember(encryptMember);

        Long memberId = encryptMember.getId();
        saveMemberAccessKey(memberId, accessToken, encryptKey, memberRequestDto.getEmail());
        authorizeMember(memberId);
    }

    @Override
    public void authorizeMember(Long memberId) {
        memberMapper.authorizeMember(memberId);
    }

    private MemberRequestDto encryptMemberInfo(MemberRequestDto memberRequestDto, String encryptKey) throws Exception {
        AESCipher aesCipher = new AESCipher(encryptKey);
        System.out.println("Member: " + memberRequestDto);
        return MemberRequestDto.builder()
                .email(aesCipher.encrypt(memberRequestDto.getEmail()))
                .password(aesCipher.encrypt(memberRequestDto.getPassword()))
                .phoneNo(aesCipher.encrypt(memberRequestDto.getPhoneNo()))
                .username(aesCipher.encrypt(memberRequestDto.getUsername()))
                .telcoTycd(memberRequestDto.getTelcoTycd())
                .registerDate(LocalDateTime.now())
                .build();
    }

    @Override
    public void saveMemberAccessKey(Long memberId, String authorization, String encryptKey, String email) {
        memberMapper.saveMemberAccessKey(memberId, authorization, encryptKey, email);
    }

    @Override
    public MemberResponseDto findMemberById(Long id) throws Exception {

        String encryptKeyByMemberId = memberMapper.findEncryptKeyByMemberId(id);
        AESCipher aesCipher = new AESCipher(encryptKeyByMemberId);

        MemberResponseDto memberById = memberMapper.findMemberById(id);
        System.out.println("findMemberByID-Test: " + memberById);
        Long authority = findMemberAuthorityByMemberId(id);
        if (authority == 1) {
            memberById.setAuthority(ROLE_ADMIN.getValue());
        } else if (authority == 2) {
            memberById.setAuthority(ROLE_MEMBER.getValue());
        }
        return MemberResponseDto.builder()
                .memberId(id)
                .username(aesCipher.decrypt(memberById.getUsername()))
                .email(aesCipher.decrypt(memberById.getEmail()))
                .phoneNo(parsingPhoneNo(aesCipher.decrypt(memberById.getPhoneNo())))
                .telcoTycd(memberById.getTelcoTycd())
                .memberStatus(memberById.getMemberStatus())
                .registerDate(memberById.getRegisterDate())
                .lastLoginDate(memberById.getLastLoginDate())
                .approvalDate(memberById.getApprovalDate())
                .accountStatus(memberById.getAccountStatus())
                .password(aesCipher.decrypt(memberById.getPassword()))
                .authority(memberById.getAuthority())
                .build();
    }

    @Override
    public MemberResponseDto findMemberByEmail(String email) throws Exception {
        return memberMapper.findMemberByEmail(email);
    }

    private String parsingPhoneNo(String phoneNo) {
        return phoneNo.substring(0, 3) + " - " + phoneNo.substring(3, 7) + " - " + phoneNo.substring(7);
    }

    @Override
    public String findEncryptKeyByMemberId(Long memberId) {
        return memberMapper.findEncryptKeyByMemberId(memberId);
    }

    @Override
    public AccessTokenDto findMemberKeyByEmail(String email) {
        return memberMapper.findMemberKeyByEmail(email);
    }

    @Override
    public LogInResponseDto maskingInformationByLogIn(String username, String phoneNo) throws Exception {

        return LogInResponseDto.builder()
                .username(maskingUsername(username))
                .phoneNo(maskingPhoneNumber(phoneNo))
                .build();
    }

    @Override
    public SignUpResponseDto maskingInformationBySignUp(MemberRequestDto memberRequestDto) {
        return SignUpResponseDto.builder()
                .username(maskingUsername(memberRequestDto.getUsername()))
                .phoneNo(maskingPhoneNumber(memberRequestDto.getPhoneNo()))
                .email(maskingEmail(memberRequestDto.getEmail()))
                .build();
    }

    @Override
    public MemberResponseDto maskingInformationByEdit(MemberResponseDto memberResponseDto) {
        return MemberResponseDto.builder()
                .username(maskingUsername(memberResponseDto.getUsername()))
                .phoneNo(maskingPhoneNumber(memberResponseDto.getPhoneNo()))
                .email(maskingEmail(memberResponseDto.getEmail()))
                .password(memberResponseDto.getPassword())
                .build();
    }

    @Override
    public void editMemberInformation(Long memberId, String newPassword) throws Exception {
        String encryptKeyByMemberId = findEncryptKeyByMemberId(memberId);
        AESCipher aesCipher = new AESCipher(encryptKeyByMemberId);

        System.out.println("EDIT -> memberId, newPWD: " + memberId + " " + newPassword);
        memberMapper.editMemberInformation(memberId, aesCipher.encrypt(newPassword), LocalDateTime.now());
    }

    @Override
    public List<MemberResponseDto> getListWithPaging(Map<String, Object> map) throws Exception {
        AESCipher aesCipher;
        List<MemberResponseDto> findMemberList = memberMapper.getListWithPaging(map);
        List<MemberResponseDto> memberList = new ArrayList<>();
        for (MemberResponseDto member : findMemberList) {
            String encryptKey = member.getEncryptKey();
            aesCipher = new AESCipher(encryptKey);
            memberList.add(MemberResponseDto.builder()
                    .memberId(member.getMemberId())
                    .username(aesCipher.decrypt(member.getUsername()))
                    .email(aesCipher.decrypt(member.getEmail()))
                    .phoneNo(parsingPhoneNo(aesCipher.decrypt(member.getPhoneNo())))
                    .telcoTycd(member.getTelcoTycd())
                    .memberStatus(member.getMemberStatus())
                    .registerDate(member.getRegisterDate())
                    .approvalDate(member.getApprovalDate())
                    .authority(member.getAuthority())
                    .build()
            );

            System.out.println("parsing:member: " + member);
        }
        return memberList;
    }
    @Override
    public void resetLoginFailCount(Long memberId) {
        memberMapper.resetLoginFailCount(memberId);
    }

    @Override
    public void updateLoginFailCount(int failCount, Long memberId) {
        memberMapper.updateLoginFailCount(failCount, memberId);
    }

    @Override
    public void updateMemberApproval(Long memberId) {
        memberMapper.updateMemberApproval(memberId, LocalDateTime.now());
    }

    @Override
    public void updateMemberToAdmin(Long memberId) {
        LogInResponseDto memberAuthorityInfo = findMemberAuthorityInfoByMemberId(memberId);
        System.out.println("Login: " + memberAuthorityInfo);
        memberMapper.updateMemberToAdmin(memberAuthorityInfo.getMember_authorities_mapping_id());
    }

    @Override
    public void updateLastLoginDate(Long memberId, LocalDateTime localDateTime) {
        memberMapper.updateLastLoginDate(memberId, localDateTime);
    }

    @Override
    public void activeLongTermMember(Long memberId, AccountStatus accountStatus) {
        memberMapper.activeLongTermMember(memberId, accountStatus.getValue());
    }

    @Override
    public void inactiveLongTermMember(Long memberId, LocalDateTime localDateTime, AccountStatus accountStatus) {
        memberMapper.inactiveLongTermMember(memberId, localDateTime, accountStatus.getValue());
    }

    @Override
    public void pauseMember(Long memberId, LocalDateTime localDateTime, AccountStatus accountStatus) {
        memberMapper.pauseMember(memberId, localDateTime, accountStatus.getValue());
    }

    @Override
    public void lockMember(Long memberId, LocalDateTime localDateTime, AccountStatus accountStatus) {
        memberMapper.lockMember(memberId, localDateTime, accountStatus.getValue());
    }

    @Override
    public void deleteMember(Long memberId) {
        memberMapper.deleteMember(memberId);
    }

    private String maskingUsername(String username) {
        StringBuilder maskedName = new StringBuilder(username);
        int length = username.length();
        if (length > 1) {
            for (int i = 1; i < Math.max(2, length - 1); i++) {
                maskedName.setCharAt(i, '*');
            }
        }
        System.out.println("이름: " + maskedName);
        return maskedName.toString();
    }

    private String maskingPhoneNumber(String phoneNo) {
        // 마스킹할 번호가 존재해야 하므로
        if (phoneNo != null && !"".equals(phoneNo)) {
            phoneNo = phoneNo.substring(0, 3) + "****"
                    + phoneNo.substring(phoneNo.length() - 4, phoneNo.length());
        }

        System.out.println("전화번호: " + phoneNo);
        return phoneNo;
    }

    /**
     * {3}부분 -> 3글자 이후로는
     * 모두 마스킹 처리
     *
     * @param email
     * @return
     */
    private String maskingEmail(String email) {
        String maskingEmail = email.replaceAll("(?<=.{3}).(?=.*@)", "*");
        System.out.println("이메일: " + maskingEmail);

        return maskingEmail;
    }

    private String initializeAccessKey(int index) {
        StringBuilder accessKey = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < index; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    accessKey.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    accessKey.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    accessKey.append((rnd.nextInt(10)));
                    break;
            }
        }

        return accessKey.toString();
    }

    public String isVerifiedMember(String email, String password, Long memberId) throws Exception {
        System.out.println("memberId:" + memberId);
        MemberResponseDto findMemberById = findMemberById(memberId);
        System.out.println(findMemberById);
        if(!findMemberById.getEmail().equals(email) &&
                !findMemberById.getPassword().equals(password)) {
            return NO_ACCOUNT.getValue(); // 존재하지 않는 회원입니다.
        } else if(!findMemberById.getEmail().equals(email) ||
                !findMemberById.getPassword().equals(password)) {
            return NO_MATCH_INFO.getValue(); // 로그인 정보가 일치하지 않습니다.
        } return LOGIN_SUCCESS.getValue(); // 로그인 성공
    }

    @Override
    public Long findMemberAuthorityByMemberId(Long memberId) {
        return memberMapper.findMemberAuthorityByMemberId(memberId);
    }

    @Override
    public LogInResponseDto findMemberAuthorityInfoByMemberId(Long memberId) {
        return memberMapper.findMemberAuthorityInfoByMemberId(memberId);
    }

    private boolean isSuperAdmin(String username, String email) {
        return username.equals(SUPER_ADMIN_USERNAME.getValue())
                && email.equals(SUPER_ADMIN_EMAIL.getValue());
    }
}
