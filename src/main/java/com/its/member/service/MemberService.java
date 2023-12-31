package com.its.member.service;

import com.its.member.controller.CustomExceptionHandler;
import com.its.member.dto.MemberDTO;
import com.its.member.entity.MemberEntity;
import com.its.member.repository.MemberRepository;
import com.its.member.tools.JWToken;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    public boolean isValidEmail(String email) {
        return email.contains("@");
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }


    public String join(MemberDTO memberDTO) {


        if (!isValidEmail(memberDTO.getEmail())) {
            throw new IllegalArgumentException("ERROR 400: 이메일 형식을 확인해주세요.");
        } else if (!isValidPassword(memberDTO.getPassword())) {
            throw new IllegalArgumentException("ERROR 400: 비밀번호는 8자리 이상이어야 합니다.");
        } else {
            String hashedPassword = BCrypt.hashpw(memberDTO.getPassword(), BCrypt.gensalt());
            memberDTO.setPassword(hashedPassword);
            MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
            memberRepository.save(memberEntity);

            return "CODE 201: 회원가입이 정상적으로 처리 되었습니다.";
        }

    }

    @Value("${jwt.secret}")
    private String secretKey;

    public String login(MemberDTO memberDTO) {
        if (!isValidEmail(memberDTO.getEmail())) {
            throw new IllegalArgumentException("ERROR 400: 이메일 형식을 확인해주세요.");
        } else if (!isValidPassword(memberDTO.getPassword())) {
            throw new IllegalArgumentException("ERROR 400: 비밀번호는 8자리 이상이어야 합니다.");
        } else {

            Optional<MemberEntity> byEmail = memberRepository.findByEmail(memberDTO.getEmail());
            if (byEmail.isPresent()) {
                MemberEntity memberEntity = byEmail.get();
                String hashedPassword = memberEntity.getPassword();
                if (BCrypt.checkpw(memberDTO.getPassword(), hashedPassword)) {
                    String token = JWToken.createJWT(memberDTO.getEmail(), secretKey);

                    memberEntity.setJwtToken(token);
                    memberRepository.save(memberEntity);

                    return token;
                } else {
                    throw new IllegalArgumentException("ERROR 400: 비밀번호가 일치하지 않습니다.");
                }

            } else {

                throw new CustomExceptionHandler.UserNotRegister("ERROR 401: 등록된 유저가 아닙니다.");
            }
        }
    }


}
