package com.its.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private MemberDTO memberDTO;
    private String email;

    public BoardDTO(Long id, String title, String content, String email) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.email = email;
    }


    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setTitle(boardEntity.getTitle());
        boardDTO.setContent(boardEntity.getContent());
        boardDTO.setMemberDTO(MemberDTO.toMemberDTO(boardEntity.getMember()));

        if (boardEntity.getMember() != null) {
            boardDTO.setEmail(boardEntity.getMember().getEmail());
        }
        return boardDTO;

    }
}
