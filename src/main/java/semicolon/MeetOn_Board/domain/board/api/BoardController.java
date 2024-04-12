package semicolon.MeetOn_Board.domain.board.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.MeetOn_Board.domain.board.application.BoardService;
import semicolon.MeetOn_Board.domain.board.dto.SearchCondition;

import java.util.List;

import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 생성
     * @param createRequestDto
     * @param request
     * @return
     */
    @Operation(description = "게시글 생성")
    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody CreateRequestDto createRequestDto,
                                              HttpServletRequest request) {
        Long board = boardService.createBoard(createRequestDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(board + " Created");
    }

    /**
     * 게시글 리스트 받아오기
     * 검색 조건 username or title 둘중 하나만 제발(선택창 있어야할듯?)
     * @param title
     * @param username
     * @param pageable
     * @param request
     * @return
     */
    @Operation(description = "게시글 리스트(페이징0)")
    @GetMapping
    public ResponseEntity<Page<BoardResponseDto>> getBoardList(@RequestParam(required = false) String title,
                                                               @RequestParam(required = false) String username,
                                                               @PageableDefault Pageable pageable,
                                                               HttpServletRequest request) {
        SearchCondition searchCondition = SearchCondition.builder().title(title).username(username).build();
        Page<BoardResponseDto> boardList = boardService.getBoardList(searchCondition, pageable, request);
        return ResponseEntity.ok(boardList);
    }

    /**
     * 게시글 상세 정보
     * @param boardId
     * @param request
     * @return
     */
    @Operation(description = "게시글 상세 정보")
    @GetMapping("/info")
    public ResponseEntity<BoardDetailResponseDto> getBoardInfo(@RequestParam Long boardId, HttpServletRequest request) {
        return ResponseEntity.ok(boardService.getBoardInfo(boardId, request));
    }

    /**
     * 게시글 수정
     * title, content, isNotice, 첨부파일(일단보류)
     * @param boardId
     * @param updateRequestDto
     * @return
     */
    @Operation(description = "게시글 수정")
    @PutMapping
    public ResponseEntity<String> updateBoard(@RequestParam Long boardId,
                                              @RequestBody UpdateRequestDto updateRequestDto) {
        boardService.updateBoard(boardId, updateRequestDto);
        return ResponseEntity.ok("Ok");
    }

    /**
     * 게시글 삭제
     */
    @Operation(description = "게시글 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteBoard(@RequestParam Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("success");
    }
}
