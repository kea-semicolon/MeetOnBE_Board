package semicolon.MeetOn_Board.domain.file.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.MeetOn_Board.domain.board.BaseTimeEntity;
import semicolon.MeetOn_Board.domain.board.domain.Board;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public static File toFile(Board board, String url, int i){
        return File
                .builder()
                .board(board)
                .title("files-" + i)
                .url(url)
                .build();
    }
}
