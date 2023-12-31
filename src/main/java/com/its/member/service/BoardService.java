package com.its.member.service;

import com.its.member.dto.BoardDTO;
import com.its.member.entity.BoardEntity;
import com.its.member.entity.MemberEntity;
import com.its.member.repository.BoardRepository;
import com.its.member.repository.MemberRepository;
import com.its.member.tools.JWToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    @Value("${jwt.secret}")
    private String secretKey;

    public BoardDTO write(BoardDTO boardDTO, String token) throws Exception {


        token = token.split(" ")[1];
        String email;
        email = JWToken.getEmail(token, secretKey);

        if (boardDTO.getContent() == null || boardDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("ERROR 400: 게시글 내용을 입력해주세요.");
        } else if (boardDTO.getTitle() == null || boardDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("ERROR 400: 게시글 제목을 입력해주세요.");
        }

        MemberEntity memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("ERROR 401: 해당 이메일의 사용자가 존재하지 않습니다."));
        BoardEntity boardEntity = BoardEntity.toboardEntity(boardDTO, memberEntity);
        BoardEntity savedEntity = boardRepository.save(boardEntity);
        return BoardDTO.toBoardDTO(savedEntity);


    }


    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수

        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getTitle(), board.getContent(), board.getMember().getEmail()));

        if (boardDTOS.hasContent()) {

            return boardDTOS;
        } else {
            throw new NoSuchElementException("ERROR 404: 글 목록이 존재하지 않습니다.");
        }
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

        token = token.split(" ")[1];
        String email = JWToken.getEmail(token, secretKey);

        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            if (email.equals(boardEntity.getMember().getEmail())) {
                boardRepository.deleteById(id);
                return "CODE 200: 성공적으로 게시글을 삭제했습니다.";
            } else {
                throw new AccessDeniedException("ERROR 403: 글 작성자만 삭제할 수 있습니다.");
            }
        } else {
            throw new NoSuchElementException("ERROR 404: 해당 ID의 게시글이 존재하지 않습니다.");
        }
    }

    public String update(Long id, BoardDTO boardDTO, String token) {

        token = token.split(" ")[1];
        String email = JWToken.getEmail(token, secretKey);

        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            if (email.equals(boardEntity.getMember().getEmail())) {
                boardEntity.setTitle(boardDTO.getTitle());
                boardEntity.setContent(boardDTO.getContent());

                boardRepository.save(boardEntity);
                return "CODE 201: 성공적으로 게시글을 수정했습니다.";
            } else {
                throw new AccessDeniedException("ERROR 403: 글 작성자만 수정할 수 있습니다.");
            }
        } else {
            throw new NoSuchElementException("ERROR 404: 해당 ID의 게시글이 존재하지 않습니다.");
        }
    }
}
