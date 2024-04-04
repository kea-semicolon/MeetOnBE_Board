package semicolon.MeetOn_Board.domain.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.MeetOn_Board.domain.board.BaseTimeEntity;
import semicolon.MeetOn_Board.domain.board.dto.BoardDto;

import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

@Getter
@Entity
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private boolean isNotice;
    private Long memberId;
    private Long channelId;

    @Builder
    public Board(Long id, String title, String content, boolean isNotice, Long memberId, Long channelId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isNotice = isNotice;
        this.memberId = memberId;
        this.channelId = channelId;
    }

    public static Board toBoard(CreateRequestDto createRequestDto, Long memberId, Long channelId) {
        return Board
                .builder()
                .title(createRequestDto.getTitle())
                .content(createRequestDto.getContent())
                .isNotice(createRequestDto.isNotice())
                .memberId(memberId)
                .channelId(channelId)
                .build();
    }
}
