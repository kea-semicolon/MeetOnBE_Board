package semicolon.MeetOn_Board.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardMemberDto {

    private Long id;
    private String username;

    @Builder
    public BoardMemberDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
