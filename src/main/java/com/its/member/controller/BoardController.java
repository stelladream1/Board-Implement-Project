package com.its.member.controller;


import com.its.member.dto.BoardDTO;
import com.its.member.service.BoardService;
import com.its.member.tools.JWToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.security.SignatureException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")

public class BoardController {

    private final BoardService boardService;

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> post(@RequestBody BoardDTO boardDTO, @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        if (token == null) {
            response.put("message", "ERROR 403: 인증되지 않은 요청입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            BoardDTO writeResult = boardService.write(boardDTO, token);
            response.put("message", "CODE 201: 게시글이 성공적으로 등록되었습니다");
            response.put("post", writeResult);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);


        } catch (AccessDeniedException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("message", "ERROR 500: 예상치 못한 오류로 게시글 작성을 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> List(@PageableDefault(page = 1) Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try {
            Page<BoardDTO> boardList = boardService.paging(pageable);

            int blockLimit = 3;
            int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), boardList.getTotalPages());
            response.put("message", "CODE 200: 성공적으로 글 목록을 조회했습니다.");
            response.put("List", boardList.getContent());

            response.put("pageNumber", pageable.getPageNumber());
            response.put("startPage", startPage);
            response.put("endPage", endPage);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "ERROR 500: 예상치 못한 오류로 게시글 조회에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();
        try {

            BoardDTO boardDTO = boardService.findById(id);
            if (boardDTO != null) {
                response.put("board", boardDTO);
                response.put("message", "CODE 200: 게시글을 성공적으로 조회하였습니다.");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("message", "해당 게시글이 없습니다");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("message", "예상치 못한 오류로 게시글 조회에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody BoardDTO boardDTO, @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        if (token == null) {
            response.put("message", "ERROR 403: 인증되지 않은 요청입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            String result = boardService.update(id, boardDTO, token);
            BoardDTO boardDTO1 = boardService.findById(id);
            response.put("message", result);
            response.put("board", boardDTO1);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (AccessDeniedException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        catch (NoSuchElementException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            response.put("message", "ERROR 500: 예상치 못한 오류로 게시글 수정에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        if (token == null) {
            response.put("message", "ERROR 403: 인증되지 않은 요청입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        try {
            String result = boardService.delete(id, token);
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (AccessDeniedException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        catch (Exception e) {
            response.put("message", "ERROR 500: 예상치 못한 오류로 게시글 삭제에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

}
