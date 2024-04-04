package semicolon.MeetOn_Board.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
