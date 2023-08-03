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
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTEwMjUwNzMsImV4cCI6MTY5MTM4NTA3M30.5szQKK_VZaFo-J9BBFjA2DeBuoh-DXiujG2luUZvZnI";

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
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTEwMjUwNzMsImV4cCI6MTY5MTM4NTA3M30.5szQKK_VZaFo-J9BBFjA2DeBuoh-DXiujG2luUZvZnI";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("게시글 제목을 입력해주세요."));

    }

    @DisplayName("게시글 목록 테스트")
    @Test
    void listSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list")
                .param("page", "1")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @DisplayName("특정 게시글 목록 테스트 성공")
    @Test
    void findByIdSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list/2")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("게시글을 성공적으로 조회하였습니다."));
    }

    @DisplayName("특정 게시글 목록 테스트 실패")
    @Test
    void findByIdFail() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list/100")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fail").value("해당 게시글이 없습니다"));
    }


    @DisplayName("게시글 수정 성공 테스트")
    @Test
    void updateSuccess() throws Exception {
        String jsonRequest = "{\"title\":\"수정중입니다\",\"content\":\"수정중입니다\"}";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTEwMjUwNzMsImV4cCI6MTY5MTM4NTA3M30.5szQKK_VZaFo-J9BBFjA2DeBuoh-DXiujG2luUZvZnI";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/update/2")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("성공적으로 게시글을 수정했습니다."));

    }

    @DisplayName("게시글 수정 실패 테스트")
    @Test
    void updateFail() throws Exception {
        String jsonRequest = "{\"title\":\"수정중입니다\",\"content\":\"수정중입니다\"}";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTEwMjUwNzMsImV4cCI6MTY5MTM4NTA3M30.5szQKK_VZaFo-J9BBFjA2DeBuoh-DXiujG2luUZvZnI";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/update/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("게시글 작성자만 수정할 수 있습니다."));

    }


    @DisplayName("게시글 삭제 성공 테스트")
    @Test
    void deleteSuccess() throws Exception {

        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTEwMjUwNzMsImV4cCI6MTY5MTM4NTA3M30.5szQKK_VZaFo-J9BBFjA2DeBuoh-DXiujG2luUZvZnI";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/2")
                        .header("Authorization", token)

                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("성공적으로 게시글을 삭제했습니다."));

    }

    @DisplayName("게시글 삭제 실패 테스트")
    @Test
    void deleteFail() throws Exception {
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQHRlc3QiLCJpYXQiOjE2OTEwMjUwNzMsImV4cCI6MTY5MTM4NTA3M30.5szQKK_VZaFo-J9BBFjA2DeBuoh-DXiujG2luUZvZnI";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/1")
                        .header("Authorization", token)

                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("게시글 작성자만 삭제할 수 있습니다."));

    }
}