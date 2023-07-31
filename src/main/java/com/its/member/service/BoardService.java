package com.its.member.service;

import com.its.member.dto.BoardDTO;
import com.its.member.entity.BoardEntity;
import com.its.member.repository.BoardRepository;
import com.its.member.tools.JWToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

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


    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수

        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        // 목록: id, writer, title, hits, createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getTitle(), board.getContent(),  board.getEmail()));
        return boardDTOS;
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);

            return boardDTO;
        } else {
            return null;
        }
    }

    public String delete(Long id, String token) {
        try {
            token = token.split(" ")[1];
            String email = JWToken.getEmail(token, secretKey);

            Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
            if (optionalBoardEntity.isPresent()) {
                BoardEntity boardEntity = optionalBoardEntity.get();
                if (email.equals(boardEntity.getEmail())) {
                    boardRepository.deleteById(id);
                    return "성공적으로 게시글을 삭제했습니다.";
                } else {
                    return "게시글 작성자만 삭제할 수 있습니다.";
                }
            } else {
                return "해당 ID의 게시글이 존재하지 않습니다.";
            }
        } catch (NoSuchElementException e) {
            // NoSuchElementException 예외 처리
            return "요청한 게시글 ID가 잘못되었습니다.";
        } catch (Exception e) {
            // 기타 예외 처리
            return "예상치 못한 오류가 발생했습니다.";
        }
    }

    public String update(Long id, BoardDTO boardDTO, String token) {
        try {
            token = token.split(" ")[1];
            String email = JWToken.getEmail(token, secretKey);

            Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
            if (optionalBoardEntity.isPresent()) {
                BoardEntity boardEntity = optionalBoardEntity.get();
                if (email.equals(boardEntity.getEmail())) {
                    boardEntity.setTitle(boardDTO.getTitle());
                    boardEntity.setContent(boardDTO.getContent());

                    boardRepository.save(boardEntity);
                    return "성공적으로 게시글을 수정했습니다.";
                } else {
                    return "게시글 작성자만 수정할 수 있습니다.";
                }
            } else {
                return "해당 ID의 게시글이 존재하지 않습니다.";
            }
        } catch (NoSuchElementException e) {
            // NoSuchElementException 예외 처리
            return "요청한 게시글 ID가 잘못되었습니다.";
        } catch (Exception e) {
            // 기타 예외 처리
            return "예상치 못한 오류가 발생했습니다.";
        }
    }
}
