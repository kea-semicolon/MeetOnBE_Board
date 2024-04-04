package semicolon.MeetOn_Board.domain.board.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.MeetOn_Board.domain.board.application.BoardService;

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
    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody CreateRequestDto createRequestDto,
                                              HttpServletRequest request) {
        Long board = boardService.createBoard(createRequestDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(board + " Created");
    }
}
