package semicolon.MeetOn_Board.domain.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.MeetOn_Board.domain.board.BaseTimeEntity;
import semicolon.MeetOn_Board.domain.file.domain.File;

import java.util.ArrayList;
import java.util.List;

import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

@Getter
@Entity
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private boolean isNotice;
    private Long memberId;
    private Long channelId;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> fileList = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, boolean isNotice, Long memberId, Long channelId, List<File> fileList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isNotice = isNotice;
        this.memberId = memberId;
        this.channelId = channelId;
        this.fileList = fileList;
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

    public void update(UpdateRequestDto updateRequestDto, List<File> fileList) {
        this.title = updateRequestDto.getTitle();
        this.content = updateRequestDto.getContent();
        this.isNotice = updateRequestDto.isNotice();
        this.fileList = fileList;
    }

    public void uploadFile(List<File> fileList) {
        this.fileList = fileList;
    }
}
