package com.example.aton_final_project.service;

import com.example.aton_final_project.model.dao.InquiryMapper;
import com.example.aton_final_project.model.dao.MemberMapper;
import com.example.aton_final_project.model.dao.ServiceRegisterMapper;
import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.service.file.FileService;
import com.example.aton_final_project.service.file.inquiry.InquiryService;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.util.constants.DataTypeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.aton_final_project.util.constants.DataTypeConstants.*;

@Service
@Transactional
public class BoardService {

    @Autowired private MemberMapper memberMapper;
    @Autowired private ServiceRegisterMapper serviceRegisterMapper;
    @Autowired private InquiryMapper inquiryMapper;
    @Autowired private MemberService memberService;
    @Autowired private FileService fileService;
    @Autowired private InquiryService inquiryService;

    // paging
    public Map<String, Object> boardList(int currentPage, String searchType, String keyword, DataTypeConstants type) throws Exception {

        // 페이지에 보여줄 행의 개수 ROW_PER_PAGE = 5으로 고정
        final int ROW_PER_PAGE = 5;

        // 페이지에 보여줄 첫번째 페이지 번호는 1로 초기화
        int startPageNum = 1;

        // 처음 보여줄 마지막 페이지 번호는 5
        int lastPageNum = ROW_PER_PAGE;

        // 현재 페이지가 ROW_PER_PAGE/2 보다 클 경우
        if (currentPage > (ROW_PER_PAGE / 2)) {
            // 보여지는 페이지 첫번째 페이지 번호는 현재페이지 - ((마지막 페이지 번호/2) -1 )
            // ex 현재 페이지가 6이라면 첫번째 페이지번호는 2
            startPageNum = currentPage - ((lastPageNum / 2) - 1);
            // 보여지는 마지막 페이지 번호는 현재 페이지 번호 + 현재 페이지 번호 - 1
            lastPageNum += (startPageNum - 1);
        }

        // Map Data Type 객체 참조 변수 map 선언
        // HashMap() 생성자 메서드로 새로운 객체를 생성, 생성된 객체의 주소값을 객체 참조 변수에 할당
        Map<String, Object> map = new HashMap<String, Object>();
        // 한 페이지에 보여지는 첫번째 행은 (현재페이지 - 1) * 10
        int startRow = (currentPage - 1) * ROW_PER_PAGE;
        // 값을 map에 던져줌
        map.put("startRow", startRow);
        map.put("rowPerPage", ROW_PER_PAGE);
        if(searchType != null) {
            map.put("searchType", searchType);
        }
        if(keyword != null) {
            map.put("keyword", keyword);
            System.out.println(keyword);
        }
        /**
         * 추상 클래스 상속 클래스에서 구현
         */
        // 구성한 값들을 Map Date Type의 resultMap 객체 참조 변수에 던져주고 return
        Map<String, Object> resultMap = new HashMap<String, Object>();

        /**
         * 추상 클래스 상속 클래스에서 구현
         */
        double boardCount = 0;
        if(type.equals(SERVICE)) {
            List<MemberServiceRegisterResponseDto> list = fileService.decryptUsername(serviceRegisterMapper.getListWithPaging(map));
            // DB 행의 총 개수를 구하는 getBoardAllCount() 메서드를 호출하여 double Date Type의 boardCount 변수에 대입
            boardCount = serviceRegisterMapper.count();
            resultMap.put("list", list);
        } else if(type.equals(INQUIRY)) {
            List<InquiryRegisterResponseDto> list = inquiryService.decryptUsername(inquiryMapper.getListWithPaging(map));
            // DB 행의 총 개수를 구하는 getBoardAllCount() 메서드를 호출하여 double Date Type의 boardCount 변수에 대입
            boardCount = inquiryMapper.count();
            resultMap.put("list", list);
        } else if(type.equals(MEMBER_APPROVAL) || type.equals(ADMIN_REGISTER)) {
            List<MemberResponseDto> list = memberService.getListWithPaging(map);
            // DB 행의 총 개수를 구하는 getBoardAllCount() 메서드를 호출하여 double Date Type의 boardCount 변수에 대입
            boardCount = memberMapper.count();
            resultMap.put("list", list);
        }

        // 마지막 페이지번호를 구하기 위해 총 개수 / 페이지당 보여지는 행의 개수 -> 올림 처리 -> lastPage 변수에 대입
        int lastPage = (int) (Math.ceil(boardCount / ROW_PER_PAGE));

        // 현재 페이지가 (마지막 페이지-4) 보다 같거나 클 경우
        if (currentPage >= (lastPage - 4)) {
            // 마지막 페이지 번호는 lastPage
            lastPageNum = lastPage;
        }

        resultMap.put("currentPage", currentPage);
        resultMap.put("lastPage", lastPage);
        resultMap.put("startPageNum", startPageNum);
        resultMap.put("lastPageNum", lastPageNum);

        return resultMap;
    }
}
