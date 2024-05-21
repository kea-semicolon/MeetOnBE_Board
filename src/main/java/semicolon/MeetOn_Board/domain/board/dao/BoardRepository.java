package semicolon.MeetOn_Board.domain.board.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.board.dto.BoardDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDslRepository {

//    Page<Board> findByChannelId(Long channelId, Pageable pageable);
    @Query("select b from Board b left join fetch File f on b = f.board where b.id = :id")
    Optional<Board> findAllInfoById(Long id);

    @Modifying
    @Query("delete from Board b where b.memberId = :memberId")
    int deleteBoardsByMemberId(Long memberId);

    List<Board> findAllByMemberId(Long memberId);
}
