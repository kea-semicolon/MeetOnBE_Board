package semicolon.MeetOn_Board.domain.board.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;
import semicolon.MeetOn_Board.domain.board.application.BoardService;
import semicolon.MeetOn_Board.domain.board.dto.SearchCondition;

import java.io.IOException;
import java.util.List;

import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ObjectMapper objectMapper;

    /**
     * 게시륵 생성
     * @param createRequestDtoString
     * @param files
     * @param request
     * @return
     * @throws IOException
     */
    @Operation(summary = "게시글 생성", description = "게시글 생성 + CreateRequestDto")
    @PostMapping
    public ResponseEntity<String> createBoard(@RequestPart String createRequestDtoString,
                                              @RequestPart(required = false) List<MultipartFile> files,
                                              HttpServletRequest request) throws IOException {
        log.info("files = {}", files.size());
        CreateRequestDto createRequestDto = objectMapper.readValue(createRequestDtoString, CreateRequestDto.class);
        Long board = boardService.createBoard(createRequestDto, files, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(board + " Created");
    }

    /**
     * 게시글 수정
     * @param boardId
     * @param files
     * @param updateRequestDtoString
     * @return
     * @throws IOException
     */
    @Operation(summary = "게시글 수정", description = "게시글 수정 + UpdateRequestDto")
    @PutMapping("/{boardId}")
    public ResponseEntity<String> updateBoard(@PathVariable Long boardId,
                                              @RequestPart(required = false) List<MultipartFile> files,
                                              @RequestPart String updateRequestDtoString) throws IOException {
        UpdateRequestDto updateRequestDto = objectMapper.readValue(updateRequestDtoString, UpdateRequestDto.class);
        boardService.updateBoard(boardId, updateRequestDto, files);
        return ResponseEntity.ok("Ok");
    }

//    /**
//     * 게시글 생성
//     * @param createRequestDto
//     * @param request
//     * @return
//     */
//    @Operation(summary = "게시글 생성", description = "게시글 생성 + CreateRequestDto")
//    @PostMapping
//    public ResponseEntity<String> createBoard(@RequestBody CreateRequestDto createRequestDto,
//                                              HttpServletRequest request) {
//        Long board = boardService.createBoard(createRequestDto, request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(board + " Created");
//    }

    /**
     * 게시글 리스트 받아오기
     * 검색 조건 username or title 둘중 하나만 제발(선택창 있어야할듯?)
     * @param title
     * @param username
     * @param pageable
     * @param request
     * @return
     */
    @Operation(summary = "게시글 리스트", description = "게시글 리스트(페이징0) + title or username 조건 + page, size")
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
    @Operation(summary = "게시글 상세 정보", description = "게시글 상세 정보")
    @GetMapping("/info")
    public ResponseEntity<BoardDetailResponseDto> getBoardInfo(@RequestParam Long boardId, HttpServletRequest request) {
        return ResponseEntity.ok(boardService.getBoardInfo(boardId, request));
    }

//    /**
//     * 게시글 수정
//     * title, content, isNotice, 첨부파일(일단보류)
//     * @param boardId
//     * @param updateRequestDto
//     * @return
//     */
//    @Operation(summary = "게시글 수정", description = "게시글 수정 + UpdateRequestDto")
//    @PutMapping
//    public ResponseEntity<String> updateBoard(@RequestParam Long boardId,
//                                              @RequestBody UpdateRequestDto updateRequestDto) {
//        boardService.updateBoard(boardId, updateRequestDto);
//        return ResponseEntity.ok("Ok");
//    }

    /**
     * 게시글 삭제
     */
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteBoard(@RequestParam Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("success");
    }
}
