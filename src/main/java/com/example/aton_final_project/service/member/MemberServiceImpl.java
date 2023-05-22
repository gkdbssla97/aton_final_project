package com.example.aton_final_project.service.member;

import com.example.aton_final_project.model.dao.MemberMapper;
import com.example.aton_final_project.model.domain.member.MemberAuthoritiesCode;
import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.model.dto.statistics.MembershipGrowthDto;
import com.example.aton_final_project.util.AESCipher;
import com.example.aton_final_project.util.constants.AccountStatus;
import com.example.aton_final_project.util.constants.LoginConstants;
import com.example.aton_final_project.util.error.code.AuthenticationLoginError;
import com.example.aton_final_project.util.error.code.AuthenticationRegistrationError;
import com.example.aton_final_project.util.error.customexception.LoginCustomException;
import com.example.aton_final_project.util.error.customexception.RegisterCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.aton_final_project.util.constants.AccessConstants.ACCESS_TOKEN;
import static com.example.aton_final_project.util.constants.AccessConstants.ENCRYPT_KEY;
import static com.example.aton_final_project.util.constants.AccountStatus.*;
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
        if (!StringUtils.hasText(memberRequestDto.getUsername())) {
            throw new RegisterCustomException(AuthenticationRegistrationError.MISSING_REQUIRED_ITEM, USERNAME.getValue());
        } else if (!StringUtils.hasText(memberRequestDto.getPhoneNo())) {
            throw new RegisterCustomException(AuthenticationRegistrationError.MISSING_REQUIRED_ITEM, PHONE_NO.getValue());
        } else if (!StringUtils.hasText(memberRequestDto.getEmail())) {
            throw new RegisterCustomException(AuthenticationRegistrationError.MISSING_REQUIRED_ITEM, EMAIL.getValue());
        } else if (!StringUtils.hasText(memberRequestDto.getPassword())) {
            throw new RegisterCustomException(AuthenticationRegistrationError.MISSING_REQUIRED_ITEM, PASSWORD.getValue());
        }
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
//        System.out.println("Member: " + memberRequestDto);
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
        if (!StringUtils.hasText(encryptKeyByMemberId)) {
            throw new LoginCustomException(AuthenticationLoginError.USER_NOT_FOUND);
        }
        AESCipher aesCipher = new AESCipher(encryptKeyByMemberId);

        MemberResponseDto memberById = memberMapper.findMemberById(id);
//        System.out.println("findMemberByID-Test: " + memberById);
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
                .adminApprovalDate(memberById.getAdminApprovalDate())
                .build();
    }

    @Override
    public MemberResponseDto findMemberByEmail(String email) throws Exception {
        MemberResponseDto findMember = memberMapper.findMemberByEmail(email);
        if (findMember != null) {
            return findMember;
        }
        throw new LoginCustomException(AuthenticationLoginError.USER_NOT_FOUND);
    }

    private String parsingPhoneNo(String phoneNo) {
        return phoneNo.substring(0, 3) + " - " + phoneNo.substring(3, 7) + " - " + phoneNo.substring(7);
    }

    @Override
    public String findEncryptKeyByMemberId(Long memberId) {
        String encryptKeyByMemberId = memberMapper.findEncryptKeyByMemberId(memberId);
        if (StringUtils.hasText(encryptKeyByMemberId)) {
            return encryptKeyByMemberId;
        }
        throw new LoginCustomException(AuthenticationLoginError.USER_NOT_FOUND);
    }

    @Override
    public int findLoginFailureCountByMemberId(Long memberId) {
        return memberMapper.findLoginFailureCountByMemberId(memberId);
    }

    @Override
    public AccessTokenDto findMemberKeyByEmail(String email) {
        try {
            AccessTokenDto memberKeyByEmail = memberMapper.findMemberKeyByEmail(email);

            if (memberKeyByEmail != null) {
                return memberKeyByEmail;
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        throw new LoginCustomException(AuthenticationLoginError.USER_NOT_FOUND);
    }

    @Override
    public LogInResponseDto maskingInformationByLogIn(MemberResponseDto memberResponseDto) throws Exception {

        return LogInResponseDto.builder()
                .username(maskingUsername(memberResponseDto.getUsername()))
                .phoneNo(maskingPhoneNumber(memberResponseDto.getPhoneNo()))
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
        if (!StringUtils.hasText(encryptKeyByMemberId)) {
            throw new LoginCustomException(AuthenticationLoginError.USER_NOT_FOUND);
        }
        AESCipher aesCipher = new AESCipher(encryptKeyByMemberId);

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
        }
        return memberList;
    }

    @Override
    public void resetLoginFailCount(Long memberId) {
        memberMapper.resetLoginFailCount(memberId);
    }

    @Override
    public void updateLoginFailCount(int failCount, Long memberId) {
//        System.out.println("addLoginFailCount: " + memberId + " " + failCount);
        memberMapper.updateLoginFailCount(failCount, memberId);
    }

    @Override
    public void updateMemberApproval(Long memberId) {
        memberMapper.updateMemberApproval(memberId, LocalDateTime.now());
    }

    @Override
    public void updateMemberToAdmin(Long memberId) {
        LogInResponseDto memberAuthorityInfo = findMemberAuthorityInfoByMemberId(memberId);
//        System.out.println("Login: " + memberAuthorityInfo);
        memberMapper.updateMemberToAdmin(memberAuthorityInfo.getMember_authorities_mapping_id(), LocalDateTime.now());
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
//        System.out.println("이름: " + maskedName);
        return maskedName.toString();
    }

    private String maskingPhoneNumber(String phoneNo) {
        // 마스킹할 번호가 존재해야 하므로
        if (phoneNo != null && !"".equals(phoneNo)) {
            phoneNo = phoneNo.substring(0, 3) + "****"
                    + phoneNo.substring(phoneNo.length() - 4, phoneNo.length());
        }

//        System.out.println("전화번호: " + phoneNo);
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
//        System.out.println("이메일: " + maskingEmail);

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
//        System.out.println("memberId:" + memberId);
        MemberResponseDto findMemberById = findMemberById(memberId);
//        System.out.println(findMemberById);
        if (!findMemberById.getEmail().equals(email) &&
                !findMemberById.getPassword().equals(password)) {
            return NO_ACCOUNT.getValue(); // 존재하지 않는 회원입니다.
        } else if (!findMemberById.getEmail().equals(email)) {
            return NO_MATCH_INFO_ID.getValue(); // ID 정보가 일치하지 않습니다.
        } else if (!findMemberById.getPassword().equals(password)) {
            return NO_MATCH_INFO_PWD.getValue(); // PWD 정보가 일치하지 않습니다.
        }
        return LOGIN_SUCCESS.getValue(); // 로그인 성공
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

    @Override
    public void verificationMemberAccessAuthority(MemberRequestDto memberRequestDto, AccessTokenDto findAccessToken, MemberResponseDto findMember) throws Exception {
        String verifiedMember = isVerifiedMember(memberRequestDto.getEmail(), memberRequestDto.getPassword(),
                findAccessToken.getMemberId());
        if (verifiedMember.equals(LoginConstants.NO_ACCOUNT.getValue())) {
            throw new LoginCustomException(AuthenticationLoginError.USER_NOT_FOUND);
        } else if (verifiedMember.equals(LoginConstants.NO_MATCH_INFO_PWD.getValue())) {
            if(findMember.getAccountStatus().equals(MEMBER_LOCK.getValue())) {
                throw new LoginCustomException(AuthenticationLoginError.LOCK_ACCOUNT);
            }
            increaseLoginFailureCount(findMember);
            throw new LoginCustomException(AuthenticationLoginError.INVALID_VALUE, PASSWORD.getValue(), findMember.getLoginFailCount() + 1);
        } else if (!findMember.getMemberStatus()) {
            if (findMember.getAccountStatus().equals(MEMBER_LOCK.getValue())) {
                throw new LoginCustomException(AuthenticationLoginError.LOCK_ACCOUNT);
            } else if (findMember.getAccountStatus().equals(MEMBER_PAUSE.getValue())) {
                throw new LoginCustomException(AuthenticationLoginError.PAUSE_ACCOUNT);
            } else throw new LoginCustomException(AuthenticationLoginError.UNAPPROVED);
        }
    }

    @Override
    public void confirmLongTermInactiveMember(AccessTokenDto findAccessToken, MemberResponseDto findMember) {
        if (findMember.getLastLoginDate() != null) {
//            LocalDateTime compareDate = LocalDateTime.now().minusDays(90);
            LocalDateTime compareDate = LocalDateTime.now().minusDays(90);
            LocalDateTime lastLoginDate = findMember.getLastLoginDate();
            if (compareDate.isAfter(lastLoginDate)) {
                inactiveLongTermMember(findAccessToken.getMemberId(), LocalDateTime.now(), LONG_TERM_NO_LOGIN);
                System.out.println(findMember);
            }
        }
    }

    private void increaseLoginFailureCount(MemberResponseDto findMember) {
        int loginFailCount = findLoginFailureCountByMemberId(findMember.getMemberId()) + 1;
        updateLoginFailCount(loginFailCount, findMember.getMemberId());
        if (loginFailCount >= 5) {
            lockMember(findMember.getMemberId(), LocalDateTime.now(), MEMBER_LOCK);
        }
    }

    @Override
    public void validationLoginInfo(MemberRequestDto memberRequestDto) {
        if(memberRequestDto.getEmail() == null || memberRequestDto.getEmail().equals("")) {
            throw new LoginCustomException(AuthenticationLoginError.MISSING_REQUIRED_ITEM, LoginConstants.EMAIL.getValue());
        }
        if(memberRequestDto.getPassword() == null || memberRequestDto.getPassword().equals("")) {
            throw new LoginCustomException(AuthenticationLoginError.MISSING_REQUIRED_ITEM, LoginConstants.PASSWORD.getValue());
        }
    }

    @Override
    public List<MemberAuthoritiesMappingDto> findAuthoritiesMappingByUserId(String userId) {
        return memberMapper.findAuthoritiesMappingByUserId(userId);
    }

    @Override
    public MemberAuthoritiesCode findAuthoritiesCodeByCodeId(Long memberAuthoritiesMappingId) {
        return memberMapper.findAuthoritiesCodeByCodeId(memberAuthoritiesMappingId);
    }

    @Override
    public int countAllMember() {
        return memberMapper.count();
    }

    @Override
    public int countTodayMember() {
        return memberMapper.countTodayMember();
    }

    @Override
    public MembershipGrowthDto countMemberGrowth() {
        return memberMapper.countMemberGrowth();
    }

    @Override
    public MembershipGrowthDto countMemberLogin() {
        return memberMapper.countMemberLogin();
    }
}
