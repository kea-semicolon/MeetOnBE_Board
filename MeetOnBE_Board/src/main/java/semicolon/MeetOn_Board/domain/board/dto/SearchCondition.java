package semicolon.MeetOn_Board.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchCondition {

    private String username;
    private String title;

    @Builder
    public SearchCondition(String username, String title) {
        this.username = username;
        this.title = title;
    }
}
