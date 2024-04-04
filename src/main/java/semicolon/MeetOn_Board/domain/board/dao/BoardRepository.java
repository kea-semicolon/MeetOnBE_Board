package semicolon.MeetOn_Board.domain.board.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.MeetOn_Board.domain.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
