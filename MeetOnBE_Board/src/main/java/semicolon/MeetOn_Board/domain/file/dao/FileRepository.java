package semicolon.MeetOn_Board.domain.file.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.file.domain.File;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByBoardId(Long boardId);

    void deleteAllByBoardId(Long boardId);
}
