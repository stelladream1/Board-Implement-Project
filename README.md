# wanted-pre-onboarding-backend

## *1. 지원자의 성명*
안녕하세요 원티드 프리온보딩 8기 지원자 김현 입니다. 
<hr/>

## *2. 애플리케이션의 실행 방법(엔드포인트 호출 방법 포함)*

1. 깃허브에서 프로젝트를 다운로드 합니다.
`git clone https://github.com/stelladream1/wanted-pre-onboarding-backend.git`

2. 데이터베이스와 연결하기 위한 환경 변수를 _**application.yml**_ 파일에서 설정합니다.
   ```
   server:
    port: 8080

    spring:
    
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
    
        url: <데이터 베이스 연결 URL>
    
        username: <아이디>
        password: <비밀번호>
    
      jpa:
          database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
          open-in-view: false
          show-sql: true
          hibernate:
            ddl-auto: update
  
    jwt:
      secret: <JWT를 생성하기 위한 secret key>
   ```
   

