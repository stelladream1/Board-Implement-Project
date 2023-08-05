package com.its.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class BoardTest {


    @Autowired
    private MockMvc mockMvc;

    @DisplayName("유효한 게시글 업로드 테스트")
    @Test
    void postSuccess() throws Exception {
        String jsonRequest = "{\"title\":\"test중입니다\",\"content\":\"테스트중입니다\"}";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTExOTk4MjEsImV4cCI6MTY5MTU1OTgyMX0.FVxy4yZNnONfHAASixv9wKPjeOL2IiLzhe6yUUrw8G8";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CODE 201: 게시글이 성공적으로 등록되었습니다."));

    }
    @DisplayName("내용 미입력 게시글 업로드 테스트")
    @Test
    void postFail() throws Exception {
        String jsonRequest = "{\"content\":\"테스트중입니다\"}";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTExOTk4MjEsImV4cCI6MTY5MTU1OTgyMX0.FVxy4yZNnONfHAASixv9wKPjeOL2IiLzhe6yUUrw8G8";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/board/post")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ERROR 400: 게시글 내용을 입력해주세요."));

    }
    @DisplayName("존재하지 않는 유저 게시글 업로드 테스트")
    @Test
    void postNotRegisterUser() throws Exception {
        String jsonRequest = "{\"title\":\"test중입니다\",\"content\":\"테스트중입니다\"}";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAdGVzdCIsImlhdCI6MTY5MTExODM0OCwiZXhwIjoxNjkxNDc4MzQ4fQ.vb3gSTaK1W93J6NT-LbHS_FZL-7jdR0Lv_6XNoUWf-A";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/board/post")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ERROR 401: 해당 이메일의 사용자가 존재하지 않습니다."));

    }
    @DisplayName("게시글 목록 테스트")
    @Test
    void listSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/board/list")
                .param("page", "1")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @DisplayName("특정 게시글 목록 조회 테스트 성공")
    @Test
    void findByIdSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/board/list/7")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CODE 200: 게시글을 성공적으로 조회하였습니다."));
    }

    @DisplayName("특정 게시글 목록 조회 테스트 실패")
    @Test
    void findByIdFail() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/board/list/100")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("해당 게시글이 없습니다"));
    }


    @DisplayName("게시글 수정 성공 테스트")
    @Test
    void updateSuccess() throws Exception {
        String jsonRequest = "{\"title\":\"수정중입니다\",\"content\":\"수정중입니다\"}";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTExOTk4MjEsImV4cCI6MTY5MTU1OTgyMX0.FVxy4yZNnONfHAASixv9wKPjeOL2IiLzhe6yUUrw8G8";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/board/update/6")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CODE 201: 성공적으로 게시글을 수정했습니다."));

    }

    @DisplayName("게시글 수정 실패 테스트")
    @Test
    void updateFail() throws Exception {
        String jsonRequest = "{\"title\":\"수정중입니다\",\"content\":\"수정중입니다\"}";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InVzZXJAd2FudGVkLmNvbSIsImlhdCI6MTY5MTA1NDY1MSwiZXhwIjoxNjkxNDE0NjUxfQ.h0fgqCQJCRm6MpVDuQ94A0JFj-ts29GbhWsSSR8MzCM";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/board/update/8")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ERROR 403: 글 작성자만 수정할 수 있습니다."));

    }


    @DisplayName("게시글 삭제 성공 테스트")
    @Test
    void deleteSuccess() throws Exception {

        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTExOTk4MjEsImV4cCI6MTY5MTU1OTgyMX0.FVxy4yZNnONfHAASixv9wKPjeOL2IiLzhe6yUUrw8G8";
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/board/delete/8")
                        .header("Authorization", token)

                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CODE 200: 성공적으로 게시글을 삭제했습니다."));

    }

    @DisplayName("게시글 삭제 실패 테스트")
    @Test
    void deleteFail() throws Exception {
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InVzZXJAd2FudGVkLmNvbSIsImlhdCI6MTY5MTA1NDY1MSwiZXhwIjoxNjkxNDE0NjUxfQ.h0fgqCQJCRm6MpVDuQ94A0JFj-ts29GbhWsSSR8MzCM";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/board/delete/9")
                        .header("Authorization", token)

                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ERROR 403: 글 작성자만 삭제할 수 있습니다."));

    }
}