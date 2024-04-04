package semicolon.MeetOn_Board.domain.board.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import semicolon.MeetOn_Board.domain.board.dao.BoardRepository;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.board.dto.BoardDto;
import semicolon.MeetOn_Board.domain.board.dto.BoardMemberDto;
import semicolon.MeetOn_Board.domain.board.dto.SearchCondition;
import semicolon.MeetOn_Board.global.exception.BusinessLogicException;
import semicolon.MeetOn_Board.global.exception.code.ExceptionCode;
import semicolon.MeetOn_Board.global.util.CookieUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 게시글 리스트 출력
     * 검색 조건(x, 작성자 이름, 제목)
     * @param searchCondition
     * @param pageable
     * @param request
     * @return
     */
    public Page<BoardResponseDto> getBoardList(SearchCondition searchCondition, Pageable pageable,
                                               HttpServletRequest request) {
        Long channelId = Long.valueOf(cookieUtil.getCookieValue("channelId", request));
        String accessToken = request.getHeader("Authorization");
        if(!boardChannelService.channelExists(channelId, accessToken)){
            throw new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND);
        }

        //조건에 맞는 유저 이름, 아이디 가져오기
        List<BoardMemberDto> memberDtoList = boardMemberService.getMemberInfo(searchCondition.getUsername(), channelId, accessToken);

        //BoardMemberDto 리스트에서 Id만 꺼내기
        List<Long> memberIdList = memberDtoList
                .stream().map((BoardMemberDto::getId)).toList();
        //Id랑 username 매핑
        Map<Long, String> memberDtoMap = memberDtoList
                .stream().collect(Collectors.toMap(BoardMemberDto::getId, BoardMemberDto::getUsername));

        Page<Board> boardPage = boardRepository.findByChannelId(searchCondition.getTitle(), memberIdList, channelId, pageable);
        List<Board> boardList = boardPage.getContent();

        //boardList에 유저 이름 붙여서 출력
        List<BoardResponseDto> result = boardList
                .stream()
                .map(board -> new BoardResponseDto(
                        board.getId(),
                        board.isNotice(),
                        board.getTitle(),
                        memberDtoMap.get(board.getMemberId()),
                        board.getCreatedAt()
                )).toList();
        return new PageImpl<>(result, pageable, boardPage.getTotalElements());
    }

    @Transactional
    public void updateBoard(Long boardId, UpdateRequestDto updateRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        board.update(updateRequestDto);
    }
}
