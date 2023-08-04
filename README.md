# wanted-pre-onboarding-backend

## *1. 지원자의 성명*

안녕하세요 원티드 프리온보딩 8기 지원자 김현 입니다. 

<hr/>

## *2. 애플리케이션의 실행 방법(엔드포인트 호출 방법 포함)*

- #### 애플리케이션 실행 방법

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

3. 프로젝트를 빌드합니다.
   ` ./gradlew build -x test `

4. 애플리케이션 실행합니다.

   `java -jar build/libs/member-0.0.1-SNAPSHOT.jar `
   **Warning**:  /build/libs 폴더에 프로젝트를 빌드한 jar 파일이 있는 지 확인해야 합니다.

- #### 앤드포인트 호출 방법

  - User

    - 회원가입

    ` POST /api/user/join` 

    - 로그인

    ` POST /api/user/login` 

  - Board

    - 게시글 작성하기

    `POST /api/board/post`

    - 게시글 목록 조회하기

    `GET /api/board/list?page=1`

    - 특정 게시글 조회하기

    `GET /api/board/list/{id}`

    - 특정 게시글 수정하기

    `PUT /api/board/update/{id}`

    - 특정 게시글 삭제하기 

    `DELETE /api/board/delete/{id}`

<hr/>

*3. 데이터베이스 테이블 구조*

![스크린샷 2023-08-03 191628](https://github.com/stelladream1/wanted-pre-onboarding-backend/assets/74993171/ffd57e5f-3e9a-4a64-85ae-29ab29d95557)
##### User Table

- user_id
  - 유저의 고유한 아이디를 저장합니다.
  - AUTO_INCREMENT로 지정되어 자동으로 증가하는 값이 할당됩니다.
  - BIGINT 타입으로 정의되어 NULL값이 올 수 없습니다. 
- user_email
  - 유저의 이메일을 저장합니다.
  - VARCHAR(255) 타입으로 지정되어 NULL값을 허용하지 않습니다. 
- user_password 
  - 유저의 비밀번호를 저장합니다. 
  - VARCHAR(255) 타입으로 지정되어 NULL값을 허용하지 않습니다. 
- jwt_token: 
  - JWT(Json Web Token)을 저장합니다. 
  - VARCHAR(500)타입으로 정의되어 NULL값을 허용합니다. 

##### Board Table

- board_id
  - 게시글의 고유한 아이디를 저장합니다.
  - AUTO_INCREMENT로 지정되어 자동으로 증가하는 값이 할당됩니다.
  - BIGINT 타입으로 정의되어 NULL값이 올 수 없습니다. 
- board_title
  - 게시글의 제목을 저장합니다.
  - VARCHAR(255) 타입으로 지정되어 NULL값을 허용하지 않습니다. 
- board_content
  - 게시글의 내용을 저장합니다. 
  - VARCHAR(1024) 타입으로 지정되어 NULL값을 허용하지 않습니다. 
- user_id: 
  - 게시글을 작성한 유저의 고유한 아이디를 저장합니다. 
  - user 테이블의 id 컬럼을 참조하는 FOREIGN KEY 입니다.
 
<hr />
