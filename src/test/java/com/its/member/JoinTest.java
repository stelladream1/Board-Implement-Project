package com.its.member;

import com.its.member.dto.MemberDTO; // Corrected the import statement for MemberDTO
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
class JoinTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("유효한 회원가입 테스트")
    @Test
    void joinSuccess() throws Exception {
        String jsonRequest = "{\"email\":\"testuser@test\",\"password\":\"12341234\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("CODE 201: 회원가입이 정상적으로 처리 되었습니다."));
    }

    @DisplayName("이메일 검증 테스트")
    @Test
    void joinIsValidEmail() throws Exception {
        String jsonRequest = "{\"email\":\"testuser\",\"password\":\"12341234\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/join") // Corrected the endpoint URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("ERROR 400: 이메일 형식을 확인해주세요."));
    }

    @DisplayName("비밀번호 검증 테스트")
    @Test
    void joinIsValidPassword() throws Exception {
        String jsonRequest = "{\"email\":\"testuser@test\",\"password\":\"1234\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/join") // Corrected the endpoint URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("ERROR 400: 비밀번호는 8자리 이상이어야 합니다."));
    }
}
