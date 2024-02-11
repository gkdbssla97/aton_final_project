# 개발자 / 관리자 센터 백오피스 페이지

## 프로젝트 개요
- 아톤(ATON) 인턴 - 관리자 / 개발자 센터 페이지 개발 프로젝트
- Front-end / Back-end Full-Stack 1인 개발 
- 개발 기간: 2023-04-28 ~ 2023-05-17
- Spring Boot 2.7.12(Gradle), MariaDB, Thymeleaf(SSR), Mybatis,
  JPA
- 이외 사용한 Tool: Logback, SMTP, git, postman

## 아키텍처
<img width="960" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/611de26c-ada9-478c-a566-e4d6bdc431d8">

## 역할
### Client
- Controller 계층에서 보낸 json 데이터로 Thymeleaf 기반 jQuery, javascript로 클라이언트 구현
- Ajax 사용하여 Server로 API 통신

### Server
- 회원가입, AES256 암호화 로그인 / 아웃(로그인 세션)
- SMTP 프로토콜 이메일 발송, 파일(img / pdf) 업로드, 다운로드
- 외부 API 활용
  - 날씨 공공 데이터 조회 (https://openweathermap.org/api)
  - kakao Map API 지도 조회 (https://apis.map.kakao.com/)

## Entity Relationship Diagram (ERD)
<img width="660" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/b1418497-724b-4e41-a9d1-8214a39f7310">

## 프로젝트 실행 화면
### 로그인 페이지 (회원가입)
- 회원가입 정보는 모두 RSA 256 암호화로 DB저장
- 회원 가입 시 개인별 접속 키 및 암호화 키 자동발급, 가입 승인 시 암호화 키 메일 발송
  
<img width="629" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/86c36a98-eb53-46b2-b28c-9dcfc856c211">

### 개발자 대시보드
- 회원의 최신 데이터(로그인, 서비스, 문의 일자) 출력
- 로그인 시 스토리지 세션 제한 10분 설정 이후 자동 로그아웃
  - .yml에서 세션 만료 600초 설정



<img width="694" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/40c1d76c-06ed-4e57-af30-5b5a91f86488">


### 서비스 신청 및 상세 페이지
- 회원이 신청한 서비스 등록 건 이미지, pdf 파일 미리보기 및 다운로드 활성화
- Image 2종은 .zip 파일로 다운로드, pdf는 .pdf 원본 다운로드
- PNG 사진 2장 사이즈 크기 제한하여 업로드
- Multipartfile로 서비스 등록 시 프로젝트 내부 디렉토리에 업로드파일 저장 및 DB에 파일경로 저장


<img width="350" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/09fc5224-8c8a-4f22-b8a3-c4d7d56be1bb">
<img width="274" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/d40c7288-66c7-45dc-9e90-7dd67083cbff">


### 관리자 대시보드
- 회원의 최신 데이터(가입 요청, 관리자 승인, 서비스, 문의 일자) 알림 및 등록, 승인 진행률 프로세스바 표시
- ROLE_ADMIN 권한 인증, 인가 시 접근 가능

<img width="698" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/3a9e1c1c-2dfb-4ed8-8b57-b9f4c86cf8c0">

### 전체 서비스 신청 내역
- 전체 회원 서비스 신청 내역 조회
- 승인 / 반려에 따른 서비스 상태 변경
  - 승인 시 SMTP Dooray 중계서버로 회원 이메일로 암호화키 / 접속키 전송
  - 반려 시 반려사유 작성하여 제출

<img width="619" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/cb3fb225-4de4-45d6-a802-5f1c40effbc0">

### 에러 코드 상태표
- Controller 계층 에러는 ExceptionalHandler(ControllerAdvice)로 처리
- 공통, 로그인, 회원가입, 서비스 등록, 문의 등록

<img width="612" alt="image" src="https://github.com/gkdbssla97/aton_final_project/assets/55674664/48a84182-2885-4889-820a-ddfefbb8af41">
