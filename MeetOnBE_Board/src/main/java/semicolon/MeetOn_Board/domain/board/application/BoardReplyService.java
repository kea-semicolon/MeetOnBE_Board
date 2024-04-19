package semicolon.MeetOn_Board.domain.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Board.domain.board.dao.BoardRepository;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.global.exception.BusinessLogicException;
import semicolon.MeetOn_Board.global.exception.code.ExceptionCode;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReplyService {

    private final BoardRepository boardRepository;

    public Boolean findBoard(Long boardId) {
        Optional<Board> find = boardRepository.findById(boardId);
        return find.isPresent();
    }

    private Board find(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
    }
}
