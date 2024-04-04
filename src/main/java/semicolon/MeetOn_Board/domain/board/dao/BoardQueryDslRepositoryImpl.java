package semicolon.MeetOn_Board.domain.board.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.board.domain.QBoard;
import semicolon.MeetOn_Board.domain.board.dto.BoardDto;
import semicolon.MeetOn_Board.domain.board.dto.SearchCondition;

import java.util.List;

import static semicolon.MeetOn_Board.domain.board.domain.QBoard.*;
import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

public class BoardQueryDslRepositoryImpl implements BoardQueryDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public BoardQueryDslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super();
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Board> findByChannelId(String title, List<Long> memberIdList, Long channelId, Pageable pageable) {
        List<Board> result =
                jpaQueryFactory.selectFrom(board)
                        .where(channelIdEq(channelId), titleContain(title), userIdIn(memberIdList))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();
        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    BooleanExpression channelIdEq(Long channelId) {
        return channelId != null ? board.channelId.eq(channelId) : null;
    }

    BooleanExpression titleContain(String title) {
        return StringUtils.hasText(title) ? board.title.contains(title) : null;
    }

    BooleanExpression userIdIn(List<Long> memberIdList) {
        return board.memberId.in(memberIdList);
    }
}
