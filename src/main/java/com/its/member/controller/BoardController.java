package com.its.member.controller;


import com.its.member.dto.BoardDTO;
import com.its.member.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class BoardController {

    private final BoardService boardService;

    @PostMapping("/post")
    public void post(@RequestBody BoardDTO boardDTO, Neo4jProperties.Authentication authentication, @RequestHeader("Authorization") String token){
        System.out.println("작성");
        boardService.write(boardDTO, token);

    }


}
