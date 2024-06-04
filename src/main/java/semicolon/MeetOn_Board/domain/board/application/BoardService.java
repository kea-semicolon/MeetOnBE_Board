package semicolon.MeetOn_Board.domain.board.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import semicolon.MeetOn_Board.domain.board.dao.BoardRepository;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.board.dto.BoardMemberDto;
import semicolon.MeetOn_Board.domain.board.dto.SearchCondition;
import semicolon.MeetOn_Board.domain.file.application.FileService;
import semicolon.MeetOn_Board.domain.file.domain.File;
import semicolon.MeetOn_Board.global.exception.BusinessLogicException;
import semicolon.MeetOn_Board.global.exception.code.ExceptionCode;
import semicolon.MeetOn_Board.global.util.CookieUtil;

import java.io.IOException;
import java.util.ArrayList;
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
    private final FileService fileService;
    private final CookieUtil cookieUtil;
    private final BoardChannelService boardChannelService;
    private final BoardMemberService boardMemberService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BoardS3Service uploadService;

    /**
     * 게시글 생성 전 memberId, channelId 유효한지 확인
     * @param createRequestDto
     * @param request
     */
    @Transactional
    public Long createBoard(CreateRequestDto createRequestDto, List<MultipartFile> files, HttpServletRequest request) throws IOException {
        Long memberId = Long.valueOf(cookieUtil.getCookieValue("memberId", request));
        Long channelId = Long.valueOf(cookieUtil.getCookieValue("channelId", request));
        String accessToken = request.getHeader("Authorization");
        log.info("isNotice={}", createRequestDto.getIsNotice());
        if (!boardMemberService.memberExists(memberId, accessToken)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        if (!boardChannelService.channelExists(channelId, accessToken)) {
            throw new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND);
        }
        Board board = Board.toBoard(createRequestDto, memberId, channelId);
        Long id = boardRepository.save(board).getId();
        List<String> fileUrls = new ArrayList<>();
        if(files != null) {
            fileUrls = uploadService.saveFiles(files, id);
        }
        List<File> fileList = new ArrayList<>();
        for(int i=0;i<fileUrls.size();i++){
            File file = File.toFile(board, fileUrls.get(i), i);
            fileList.add(file);
        }
        board.uploadFile(fileList);
        return id;
    }

    /**
     * 게시글 수정
     * @param boardId
     * @param updateRequestDto
     */
    @Transactional
    public void updateBoard(Long boardId, UpdateRequestDto updateRequestDto, List<MultipartFile> files) throws IOException {
        Board board = boardRepository.findAllInfoById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        //기존 파일 삭제
        List<File> legacyFile = board.getFileList();
        List<Long> fileIdList = legacyFile.stream()
                .map(File::getId)
                .collect(Collectors.toList());
        fileService.deleteFiles(fileIdList);
        uploadService.deleteFiles(legacyFile);

        //새 정보 업데이트
        List<String> fileUrls = new ArrayList<>();
        if(files != null){
            fileUrls = uploadService.saveFiles(files, board.getId());
        }
        List<File> fileList = new ArrayList<>();
        for(int i=0;i<fileUrls.size();i++){
            File file = File.toFile(board, fileUrls.get(i), i);
            fileList.add(file);
        }
        board.update(updateRequestDto, fileList);
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
        List<BoardMemberDto> memberDtoList =
                boardMemberService.getMemberInfoList(searchCondition.getUsername(), channelId, accessToken);

        //BoardMemberDto 리스트에서 Id만 꺼내기
        List<Long> memberIdList = memberDtoList
                .stream().map((BoardMemberDto::getId)).toList();
        //Id랑 username 매핑
        Map<Long, String> memberDtoMap = memberDtoList
                .stream().collect(Collectors.toMap(BoardMemberDto::getId, BoardMemberDto::getUsername));

        Page<Board> boardPage =
                boardRepository.findByChannelId(searchCondition.getTitle(), memberIdList, channelId, pageable);
        List<Board> boardList = boardPage.getContent();

        //boardList에 유저 이름 붙여서 출력
        List<BoardResponseDto> result = boardList
                .stream()
                .map(board -> new BoardResponseDto(
                        board.getId(),
                        board.getIsNotice(),
                        board.getTitle(),
                        memberDtoMap.get(board.getMemberId()),
                        board.getCreatedAt()
                )).toList();
        return new PageImpl<>(result, pageable, boardPage.getTotalElements());
    }

    /**
     * 게시글 상세
     * @param boardId
     * @return
     */
    public BoardDetailResponseDto getBoardInfo(Long boardId, HttpServletRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        String accessToken = request.getHeader("Authorization");
        Long memberId = board.getMemberId();
        BoardMemberDto memberInfo = boardMemberService.getMemberInfo(memberId, accessToken);
        return BoardDetailResponseDto.boardDetailResponseDto(memberInfo, board);
    }

//    /**
//     * 게시글 수정
//     * @param boardId
//     * @param updateRequestDto
//     */
//    @Transactional
//    public void updateBoard(Long boardId, UpdateRequestDto updateRequestDto) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
//        board.update(updateRequestDto);
//    }


    /**
     * 게시글 삭제 + 딸린 댓글 삭제
     * @param boardId
     */
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        fileService.deleteFile(boardId);

        boardRepository.delete(board);
        kafkaTemplate.send("board-deleted-topic", boardId.toString());
    }
}
