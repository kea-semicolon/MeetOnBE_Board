package semicolon.MeetOn_Board.domain.board.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import semicolon.MeetOn_Board.domain.board.application.BoardReplyService;

@Slf4j
@RestController
@RequestMapping("/board/reply")
@RequiredArgsConstructor
public class BoardReplyController {

    private final BoardReplyService boardReplyService;

    @GetMapping("/exist")
    public Boolean existBoard(@RequestParam Long boardId) {
        return boardReplyService.findBoard(boardId);
    }
}
