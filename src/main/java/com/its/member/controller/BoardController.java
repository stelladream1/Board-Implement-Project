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
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("message", "게시글 등록에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> List(@PageableDefault(page = 1) Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try{
            Page<BoardDTO> boardList = boardService.paging(pageable);

            int blockLimit = 3;
            int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), boardList.getTotalPages());

            response.put("List", boardList);
            response.put("startPage", startPage);
            response.put("endPage", endPage);

            return ResponseEntity.ok(response);

        }
        catch (Exception e){
            response.put("message", "게시글 조회에 실패했습니다");
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
                response.put("success", "게시글을 성공적으로 조회하였습니다.");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("fail", "해당 게시글이 없습니다");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        catch (Exception e){
            response.put("message", "게시글 조회에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }



    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody BoardDTO boardDTO, @RequestHeader("Authorization") String token) {

        Map<String, Object> response = new HashMap<>();
        try {
            String result = boardService.update(id, boardDTO, token);
            if (result.equals("성공적으로 게시글을 수정했습니다.")){
                BoardDTO boardDTO1 = boardService.findById(id);
                response.put("message", result);
                response.put("board", boardDTO1);
                return ResponseEntity.status(HttpStatus.OK).body( response);

            }
            else{
                response.put("message", result);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        }
        catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("message", "게시글 수정에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }


    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = boardService.delete(id, token);
            if (result.equals("성공적으로 게시글을 삭제했습니다.")) {
                response.put("message", result);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", result);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("message", "게시글 삭제에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
