package com.its.member.controller;


import com.its.member.dto.BoardDTO;
import com.its.member.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class BoardController {

    private final BoardService boardService;

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> post(@RequestBody BoardDTO boardDTO, @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            BoardDTO writeResult = boardService.write(boardDTO, token);
            response.put("message", "게시글이 성공적으로 등록되었습니다");
            response.put("post", writeResult);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage()); // The error message from IllegalArgumentException
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("message", "게시글 등록에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>>List(@PageableDefault(page = 1) Pageable pageable){
        Page<BoardDTO> boardList = boardService.paging(pageable);
        Map<String, Object> responseData = new HashMap<>();

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), boardList.getTotalPages());

        responseData.put("List", boardList);
        responseData.put("startPage", startPage);
        responseData.put("endPage", endPage);

        return ResponseEntity.ok(responseData);
    }

}
