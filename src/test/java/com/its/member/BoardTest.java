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
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAdGVzdCIsImlhdCI6MTY5MDg2NTU1OSwiZXhwIjoxNjkwODY5MTU5fQ.cGIsaPs0PPrHogaYIjaewMmKgnCnGNlV8sGdTFYhBkw";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("게시글이 성공적으로 등록되었습니다"));

    }
    @DisplayName("내용 또는 제목 미입력 게시글 업로드 테스트")
    @Test
    void postFail() throws Exception {
        String jsonRequest = "{\"content\":\"테스트중입니다\"}";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAdGVzdCIsImlhdCI6MTY5MDg2NTU1OSwiZXhwIjoxNjkwODY5MTU5fQ.cGIsaPs0PPrHogaYIjaewMmKgnCnGNlV8sGdTFYhBkw";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("게시글 제목을 입력해주세요."));

    }


}