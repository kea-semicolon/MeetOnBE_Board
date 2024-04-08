package semicolon.MeetOn_Board.domain.board.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.board.dto.BoardDto;

import java.util.List;
import java.util.stream.Stream;

import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDslRepository {

//    Page<Board> findByChannelId(Long channelId, Pageable pageable);
    void deleteAllByMemberId(Long memberId);

    List<Board> findAllByMemberId(Long memberId);
}
