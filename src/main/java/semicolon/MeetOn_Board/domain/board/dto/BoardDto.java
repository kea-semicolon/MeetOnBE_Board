package semicolon.MeetOn_Board.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.file.domain.File;
import semicolon.MeetOn_Board.domain.file.dto.FileDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static semicolon.MeetOn_Board.domain.file.dto.FileDto.*;

public class BoardDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRequestDto {
        private String title;
        private String content;
        private Boolean isNotice;
        //파일은 일단 보류

        @Builder
        public CreateRequestDto(String title, String content, Boolean isNotice) {
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
        private Boolean isNotice;
        private String title;
        private String content;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
        //첨부파일은 보류
        private FileResponseDtoList fileResponseDtoList;

        @Builder
        public BoardDetailResponseDto(String username, Long userId, Boolean isNotice,
                                      String title, String content, FileResponseDtoList fileResponseDtoList,
                                      LocalDateTime createdDate) {
            this.username = username;
            this.userId = userId;
            this.isNotice = isNotice;
            this.title = title;
            this.content = content;
            this.fileResponseDtoList = fileResponseDtoList;
            this.createdDate = createdDate;
        }

        public static BoardDetailResponseDto boardDetailResponseDto(BoardMemberDto boardMemberDto, Board board) {
            List<FileResponseDto> fileResponseDtos = board.getFileList().stream()
                    .map(FileResponseDto::of)
                    .collect(Collectors.toList());
            FileResponseDtoList fileResponseDtoList = FileResponseDtoList.builder()
                    .fileList(fileResponseDtos)
                    .build();
            return BoardDetailResponseDto
                    .builder()
                    .userId(boardMemberDto.getId())
                    .username(boardMemberDto.getUsername())
                    .isNotice(board.getIsNotice())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .fileResponseDtoList(fileResponseDtoList)
                    .createdDate(board.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRequestDto {
        private String title;
        private String content;
        private Boolean isNotice;
        //첨부파일은 보류
        @Builder
        public UpdateRequestDto(String title, String content, Boolean isNotice) {
            this.title = title;
            this.content = content;
            this.isNotice = isNotice;

        }
    }
}
