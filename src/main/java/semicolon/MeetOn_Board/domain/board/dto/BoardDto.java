package semicolon.MeetOn_Board.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class BoardDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRequestDto {
        private String title;
        private String content;
        private boolean isNotice;
        //파일은 일단 보류

        @Builder
        public CreateRequestDto(String title, String content, boolean isNotice) {
            this.title = title;
            this.content = content;
            this.isNotice = isNotice;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BoardResponseListDto<T> {
        private T boardList;

        @Builder
        public BoardResponseListDto(T boardList) {
            this.boardList = boardList;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BoardResponseDto {
        private Long boardId;
        private boolean isNotice;
        private String boardTitle;
        private String username;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDate;

        @Builder
        public BoardResponseDto(Long boardId, boolean isNotice, String boardTitle,
                                String username, LocalDateTime createdDate) {
            this.boardId = boardId;
            this.isNotice = isNotice;
            this.boardTitle = boardTitle;
            this.username = username;
            this.createdDate = createdDate;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BoardDetailResponseDto {
        private String username;
        private Long userId;
        private boolean isNotice;
        private String content;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Local createdDate;
        //첨부파일은 보류

    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRequestDto {
        private String title;
        private String content;
        private boolean isNotice;
        //첨부파일은 보류
        @Builder
        public UpdateRequestDto(String title, String content, boolean isNotice) {
            this.title = title;
            this.content = content;
            this.isNotice = isNotice;
        }
    }
}
