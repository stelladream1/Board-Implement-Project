package com.its.member.controller;


import com.its.member.dto.MemberDTO;
import com.its.member.repository.MemberRepository;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;



    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberDTO memberDTO){
        String message = memberService.join(memberDTO);
        try {
            if (message.equals("회원가입이 성공적으로 완료됐습니다.")) {
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에 오류가 발생했습니다.");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO memberDTO){

        return ResponseEntity.ok().body(memberService.login(memberDTO));
    }



}
