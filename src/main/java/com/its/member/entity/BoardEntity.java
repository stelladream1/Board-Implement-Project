package com.its.member.entity;


import javax.persistence.*;

@Entity
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
}
