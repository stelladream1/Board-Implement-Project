package com.its.member.entity;


import com.its.member.dto.BoardDTO;
import com.its.member.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_content",  nullable = false, length = 1024)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    private MemberEntity member;

    public static BoardEntity toboardEntity(BoardDTO boardDTO, MemberEntity memberEntity){
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        boardEntity.setMember(memberEntity);

        return boardEntity;
    }




}
