package semicolon.MeetOn_Board.domain.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Board.domain.board.dao.BoardRepository;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.file.application.FileService;
import semicolon.MeetOn_Board.global.exception.BusinessLogicException;
import semicolon.MeetOn_Board.global.exception.code.ExceptionCode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardKafkaService {

    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final static String MEMBER_DELETED_TOPIC = "member_deleted_topic";
    private final static String CHANNEL_DELETED_TOPIC = "channel_deleted_topic";
    private final static String BOARD_DELETED_TOPIC = "board_deleted_topic";

    /**
     * Member 삭제 시 Member가 작성한 모든 Board 삭제 + Board의 Reply 삭제
     * @param memberIdStr
     */
    @Transactional
    @KafkaListener(topics = MEMBER_DELETED_TOPIC, groupId = "member-group")
    public void deleteByMemberDeleted(String memberIdStr) {
        log.info("Member 삭제 memberId={}", memberIdStr);
        Long memberId = Long.valueOf(memberIdStr);

        List<Board> boardList = boardRepository.findAllByMemberId(memberId);
        for (Board board : boardList) {
            fileService.deleteFile(board.getId());
            kafkaTemplate.send(BOARD_DELETED_TOPIC, board.getId().toString());
        }
        int c = boardRepository.deleteBoardsByMemberId(memberId);
        log.info("Board {}개 삭제 완료", c);
    }

    @Transactional
    @KafkaListener(topics = CHANNEL_DELETED_TOPIC, groupId = "channel-group")
    public void deleteByChannelDeleted(String channelIdStr) {
        log.info("Channel 삭제 channelId={}", channelIdStr);
        Long channelId = Long.valueOf(channelIdStr);

        List<Board> boardList = boardRepository.findAllByChannelId(channelId);
        for (Board board : boardList) {
            fileService.deleteFile(board.getId());
            kafkaTemplate.send(BOARD_DELETED_TOPIC, board.getId().toString());
        }
        int c = boardRepository.deleteAllByChannelId(channelId);
        log.info("Board {}개 삭제 완료", c);
    }
}
