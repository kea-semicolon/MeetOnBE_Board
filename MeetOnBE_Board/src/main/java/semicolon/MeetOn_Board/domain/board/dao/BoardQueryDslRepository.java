package semicolon.MeetOn_Board.domain.board.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.board.dto.SearchCondition;

import java.util.List;

public interface BoardQueryDslRepository {

    Page<Board> findByChannelId(String title, List<Long> memberIdList, Long channelId, Pageable pageable);
}
