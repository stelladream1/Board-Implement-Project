package com.its.member.controller;


import com.its.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class BoardController {
    @PostMapping("/post")
    public ResponseEntity<String> post(@RequestBody MemberDTO memberDTO, Neo4jProperties.Authentication authentication){

        return ResponseEntity.ok().body(memberDTO.getEmail());
    }
}
