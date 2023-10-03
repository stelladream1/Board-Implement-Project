# wanted-pre-onboarding-backend

## 목차   

1. [애플리케이션의 실행 방법(엔드포인트 호출 방법 포함)](#1-애플리케이션의-실행-방법엔드포인트-호출-방법-포함)
2. [데이터베이스 테이블 구조](#2-데이터베이스-테이블-구조)
3. [구현한 API의 동작을 촬영한 데모 영상 링크](#3-구현한-api의-동작을-촬영한-데모-영상-링크)
4. [구현 방법 및 이유에 대한 간략한 설명](#4-구현-방법-및-이유에-대한-간략한-설명)
5. [API 명세(request/response 포함)](#5-api-명세requestresponse-포함)        
6. [추가 기능](#6-추가-기능)

<hr/>

사용언어: Java & Spring 

<hr/>

## *1. 애플리케이션의 실행 방법(엔드포인트 호출 방법 포함)*

- #### 애플리케이션 실행 방법

1. 깃허브에서 프로젝트를 다운로드 합니다.  
   `git clone https://github.com/stelladream1/wanted-pre-onboarding-backend.git`

2.  _**application.yml**_ 파일에서 데이터베이스와 연결하기 위한 환경 변수를 설정합니다.

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

3. _**Docker-compose.yml**_ 파일에 필요한 환경변수를 설정합니다.           

```
version: '3'

services:
  database:
    container_name: mysql_db
    image: mysql:latest
    environment:
      MYSQL_DATABASE: my_springboot_db
      MYSQL_ROOT_HOST: '%'
      MYSQL_ROOT_PASSWORD: <DB 비밀번호>
      TZ: 'Asia/Seoul'
    ports:
      - "4000:3306"

    command:
      - "mysqld"
      - "--character-set-server=utf8mb4"
      - "--collation-server=utf8mb4_unicode_ci"
    networks:
      - test_network
  application:
    container_name: docker-compose-test
    image: thedarknight2008/springbootwebapp:latest
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: <DB URL>
      SPRING_DATASOURCE_USERNAME:  <DB 아이디>
      SPRING_DATASOURCE_PASSWORD:  <DB 비밀번호>

    networks:
      - test_network
```
4. 프로젝트를 빌드합니다.             
   ` ./gradlew build -x test `

5. 애플리케이션 실행합니다.

- Docker-compose로 실행할 경우       
`docker-compose up --build `

- 내장된 웹서버로 실행할 경우        
`java -jar build/libs/member-0.0.1-SNAPSHOT.jar `                

**Warning**:  /build/libs 폴더에 프로젝트를 빌드한 jar 파일이 있는 지 확인해야 합니다.

- #### 앤드포인트 호출 방법

배포 주소: http://43.201.66.22:8080/
              
| 기능                   | HTTP METHOD | URL                       |
|-----------------------|-------------|---------------------------|
| 회원가입               | POST        | /api/user/join            |
| 로그인                 | POST        | /api/user/login           |
| 게시글 작성하기        | POST        | /api/board/post           |
| 게시글 목록 조회하기   | GET         | /api/board/list?page={pageNum}    |
| 특정 게시글 조회하기   | GET         | /api/board/list/{id}      |
| 특정 게시글 수정하기   | PUT         | /api/board/update/{id}    |
| 특정 게시글 삭제하기   | DELETE      | /api/board/delete/{id}    |
 
   
<hr/>

## *2. 데이터베이스 테이블 구조*

![스크린샷 2023-08-03 191628](https://github.com/stelladream1/wanted-pre-onboarding-backend/assets/74993171/ffd57e5f-3e9a-4a64-85ae-29ab29d95557)

 ##### User Table

- user_id
  - 유저의 고유한 아이디를 저장합니다.
  - AUTO_INCREMENT로 지정되어 자동으로 증가하는 값이 할당됩니다.
  - BIGINT 타입으로 정의되어 NULL값이 올 수 없습니다. 
- user_email
  - 유저의 이메일을 저장합니다.
  -  NULL값을 허용하지 않고 최대 255 글자까지 저장할 수 있습니다. 
- user_password 
  - 유저의 비밀번호를 저장합니다. 
  -  NULL값을 허용하지 않고 최대 255 글자까지 저장할 수 있습니다. 
- jwt_token: 
  - JWT(Json Web Token)을 저장합니다. 
  -  NULL값을 허용하고 최대 500 글자까지 저장할 수 있습니다. 

##### Board Table

- board_id
  - 게시글의 고유한 아이디를 저장합니다.
  - AUTO_INCREMENT로 지정되어 자동으로 증가하는 값이 할당됩니다.
  - BIGINT 타입으로 정의되어 NULL값이 올 수 없습니다. 
- board_title
  - 게시글의 제목을 저장합니다.
  -  NULL값을 허용하지 않고 최대 255 글자까지 저장할 수 있습니다. 
- board_content
  - 게시글의 내용을 저장합니다. 
  -  NULL값을 허용하지 않고 최대 1024 글자까지 저장할 수 있습니다. 
- user_id: 
  - 게시글을 작성한 유저의 고유한 아이디를 저장합니다. 
  - user 테이블의 id 컬럼을 참조하는 FOREIGN KEY 입니다. 
<hr />

## *3. 구현한 API의 동작을 촬영한 데모 영상 링크*
데모 영상은 [데모영상](https://drive.google.com/file/d/1isSH_qBQuyR9Qx_iRETsk0QMIzKMcf0v/view?usp=sharing) 에서 확인하실 수 있습니다.
<hr />

## *4. 구현 방법 및 이유에 대한 간략한 설명*

- MVC 패턴을 사용하여 각각의 역할을 분리시켜 유연성과 유지보수성을 높였습니다.
- JPA 인터페이스를 사용하여 간편하게 데이터베이스의 CRUD 작업을 구현하였습니다. 

*1. 회원가입 엔드포인트*
   - BCrypt 함수를 사용하여 비밀번호를 암호화, 복호화하였습니다. 
   - 단방향 함수로 salt를 사용하여 해시의 다양성을 증가시켜 더욱 안전하게 비밀번호를 저장할 수 있기에 사용하였습니다.

*2.로그인 엔드포인트*  
   - 로그인 시 BCrpy 함수로 비밀번호 일치 여부를 판단한 후 주어진 이메일 정보를 바탕으로 JWT를 생성하고 유효기간을 지정하였습니다.        
   - io.jsonwebtoken 라이브러리를 사용하여 JWT를 구현하였습니다.       
   - 쉽고 효율적으로 JWT를 생성하고 파싱할 수 있기에 사용하였습니다.

*3. 게시글 작성 엔드포인트*             
   - 유효한 JWT를 Authorization 헤더에 포함하여 보낼 경우에만 게시글 작성이 가능합니다.

*4. 게시글 목록 조회 엔드포인트*                 
   - JPA를 기반으로 하는 Pageable을 활용한 Pagination을 구현하였습니다.         
   - 쉽고 간편하게 원하는 페이지에 접근할 수 있고 정렬 방식을 지정하여 조회할 수 있기에 사용하였습니다.

*5. 특정 게시글 수정, 삭제 엔드포인트*               
   - 유효한 JWT를 Authorization 헤더에 포함하여 보낼 경우에만 게시글 수정, 삭제가 가능합니다.      
   - JWT에서 이메일 정보를 추출하여 DB에 저장된 작성자와 일치할 경우에만 수정, 삭제가 가능합니다.                

<hr />         


## *5. API 명세(request/response 포함)*
[API 명세서](https://documenter.getpostman.com/view/27435050/2s9Xxwwts8#509c06c2-3965-4cf9-9e30-7538be99e49f)

### 1. 회원가입
> Request
```
POST  http://43.201.66.22:8080/api/user/join
Body
   {
       "email": "testuser@wanted.com",
       "password": "12341234"
   }
```
> Response
```
{
   CODE 201: 회원가입이 정상적으로 처리 되었습니다.
}
```
> Error Code
```
400: 이메일 또는 비밀번호 미입력, 올바르지 않은 이메일 형식, 비밀번호 8자리 미만
500: 예상치못한 서버 오류 
```
### 2. 로그인
> Request
```
POST  http://43.201.66.22:8080/api/user/login
Body
   {
       "email": "testuser@wanted.com",
       "password": "12341234"
   }
```
> Response
```
{
   ...AcessJWT...
}
```
> Error Code
```
400: 이메일 또는 비밀번호 미입력, 올바르지 않은 이메일 형식, 비밀번호 8자리 미만
401: 등록되지 않은 유저
500: 예상치못한 서버 오류 
```
### 3. 게시글 작성
> Request
```
POST  http://43.201.66.22:8080/api/board/post

Headers
   {
      "Authorization": "Bearer ...AccessJWT..." 
   }

Body
{
    "title":"제목 test입니다.",
    "content":"내용 test입니다."
}
```
> Response
```
{
    "post": {
        "id": [게시글 번호],
        "title": "제목 test입니다.",
        "content": "내용 test입니다.",
        "email": "testuser@wanted.com"
    },
    "message": "CODE 201: 게시글이 성공적으로 등록되었습니다"
}
```
> Error Code
```
400: 제목 또는 내용 미입력 
401: 존재하지않은 사용자의 토큰으로 접근 
500: 예상치못한 서버 오류 
```
### 4. 게시글 조회
> Request
```
GET  http://43.201.66.22:8080/api/board/list?page={pageNum}
```
> Response
```
{
    "startPage": 시작 페이지,
    "pageNumber": 현재 페이지,
    "List": [
        {
            "id": 11,
            "title": "제목 test입니다.",
            "content": "내용 test입니다.",
            "email": "testuser@wanted.com"
        },
        {
            "id": 10,
            "title": "제목 test2입니다.",
            "content": "내용 test2입니다.",
            "email": "user@wanted.com"
        }
    ],
    "endPage": 마지막 페이지,
    "message": "CODE 200: 성공적으로 글 목록을 조회했습니다."
}
```
> Error Code
```
404: 게시글 목록이 존재하지 않음  
500: 예상치못한 서버 오류 
```
### 5. 특정 게시글 조회
> Request
```
GET  http://43.201.66.22:8080/api/board/list/{id}
```
> Response
```
{
    "message": "CODE 200: 게시글을 성공적으로 조회하였습니다.",
    "board": {
        "id": 12,
        "title": "제목 test입니다.",
        "content": "내용 test입니다.",
        "email": "testuser@wanted.com"
    }
}
```
> Error Code
```
404: 특정 게시글이 존재하지 않음  
500: 예상치못한 서버 오류 
```
### 6. 특정 게시글 수정
> Request
```
PUT  http://43.201.66.22:8080/api/board/update/{id}

Headers
   {
      "Authorization": "Bearer ...AccessJWT..." 
   }

Body
{
    "title": "수정 test입니다.",
    "content": "test입니다."
}
```
> Response
```
{
    "message": "CODE 201: 성공적으로 게시글을 수정했습니다.",
    "board": {
        "id": 12,
        "title": " 수정 test입니다 ",
        "content": "test입니다",
        "email": "testuser@wanted.com"
    }
}
```
> Error Code
```
401: 유효하지않은 토큰으로 접근
403: 게시글 작성자가 아닌데 수정 요청
404: 특정 게시글이 존재하지 않음  
500: 예상치못한 서버 오류 
```
### 7. 특정 게시글 삭제 
> Request
```
DELETE  http://43.201.66.22:8080/api/board/delete/{id}

Headers
   {
      "Authorization": "Bearer ...AccessJWT..." 
   }
```
> Response
```
{
    "message": "CODE 200: 성공적으로 게시글을 삭제했습니다."
}
```
> Error Code
```
401: 유효하지않은 토큰으로 접근
403: 게시글 작성자가 아닌데 삭제 요청
404: 특정 게시글이 존재하지 않음  
500: 예상치못한 서버 오류 
```
<hr /> 

      
## *6. 추가 기능*

### 1. 단위 테스트 코드 추가    
   - src/test에 단위 테스트 코드를 작성하였습니다.      
   - 201 또는 200 코드를 반환하는 성공 테스트와 실패 테스트 코드를 작성하였습니다.
   - 회원가입, 로그인, 게시글 작성, 수정,삭제, 조회에 대한 단위 테스트 코드입니다. 

### 2. docker compose를 이용하여 애플리케이션 환경을 구성한 경우
   - 자세한 사항은 [목차2](#2-애플리케이션의-실행-방법엔드포인트-호출-방법-포함)를 참고해주세요.


### 3. 클라우드 환경(AWS, GCP)에 배포 환경을 설계하고 애플리케이션을 배포한 경우
![Blank diagram (2)](https://github.com/stelladream1/wanted-pre-onboarding-backend/assets/74993171/05f93649-f25d-40b2-8400-651fdd7e3a26)
- AWS 배포 URL: http://43.201.66.22:8080/  
- 자세한 사항은 [목차6](#6-api-명세requestresponse-포함)을 참고해주세요. 

