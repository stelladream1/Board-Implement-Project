package com.its.member.dto;

import com.its.member.entity.BoardEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor  //기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자

public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String email;


    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setTitle(boardEntity.getTitle());
        boardDTO.setContent(boardEntity.getContent());
        boardDTO.setEmail(boardEntity.getEmail());

        return boardDTO;

    }
}
