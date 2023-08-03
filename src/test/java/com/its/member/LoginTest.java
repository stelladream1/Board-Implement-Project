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
class LoginTest {


    @Autowired
    private MockMvc mockMvc;

    @DisplayName("유효한 로그인 테스트")
    @Test
    void LoginSuccess() throws Exception {
        String jsonRequest = "{\"email\":\"testuser@test\",\"password\":\"12341234\"}";


        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @DisplayName("로그인시 이메일 검증 테스트")
    @Test
    void loginIsValidEmail() throws Exception {
        String jsonRequest = "{\"email\":\"testuser\",\"password\":\"12341234\"}";


        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("로그인 정보를 다시 확인해주세요."));

    }
    @DisplayName("로그인시 비밀번호 검증 테스트")
    @Test
    void loginIsValidPassword() throws Exception {
        String jsonRequest = "{\"email\":\"testuser@test\",\"password\":\"1234\"}";


        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("로그인 정보를 다시 확인해주세요."));

    }


    @DisplayName("로그인시 비밀번호 불일치 테스트")
    @Test
    void loginIsWrongPassword() throws Exception {
        String jsonRequest = "{\"email\":\"testuser@test\",\"password\":\"123412341\"}";


        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("비밀번호가 일치하지 않습니다."));

    }
}