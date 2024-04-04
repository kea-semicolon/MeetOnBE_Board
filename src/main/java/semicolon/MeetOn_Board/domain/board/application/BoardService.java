package semicolon.MeetOn_Board.domain.board.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import semicolon.MeetOn_Board.domain.board.dao.BoardRepository;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.board.dto.BoardDto;
import semicolon.MeetOn_Board.global.exception.BusinessLogicException;
import semicolon.MeetOn_Board.global.exception.code.ExceptionCode;
import semicolon.MeetOn_Board.global.util.CookieUtil;

import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final CookieUtil cookieUtil;
    private final BoardChannelService boardChannelService;
    private final BoardMemberService boardMemberService;

    /**
     * 게시글 생성 전 memberId, channelId 유효한지 확인
     * @param createRequestDto
     * @param request
     */
    @Transactional
    public Long createBoard(CreateRequestDto createRequestDto, HttpServletRequest request) {
        Long memberId = Long.valueOf(cookieUtil.getCookieValue("memberId", request));
        Long channelId = Long.valueOf(cookieUtil.getCookieValue("channelId", request));
        log.info("memberId={}, channelId={}", memberId, channelId);
        String accessToken = request.getHeader("Authorization");
        if (!boardMemberService.memberExists(memberId, accessToken)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        if (!boardChannelService.channelExists(channelId, accessToken)) {
            throw new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND);
        }

        Board board = Board.toBoard(createRequestDto, memberId, channelId);
        return boardRepository.save(board).getId();
    }
}
