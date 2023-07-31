package com.its.member.service;

import com.its.member.dto.BoardDTO;
import com.its.member.entity.BoardEntity;
import com.its.member.repository.BoardRepository;
import com.its.member.tools.JWToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Value("${jwt.secret}")
    private String secretKey;
    public BoardDTO write(BoardDTO boardDTO, String token) {
        token = token.split(" ")[1];
        String email = JWToken.getEmail(token, secretKey);
        boardDTO.setEmail(email);

        if (boardDTO.getContent() == null || boardDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("게시글 내용을 입력해주세요.");
        }
        else if (boardDTO.getTitle() == null || boardDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("게시글 내용을 입력해주세요.");
        }
        BoardEntity boardEntity = BoardEntity.toboardEntity(boardDTO);
        BoardEntity savedEntity = boardRepository.save(boardEntity);
        return BoardDTO.toBoardDTO(savedEntity);


    }
}
