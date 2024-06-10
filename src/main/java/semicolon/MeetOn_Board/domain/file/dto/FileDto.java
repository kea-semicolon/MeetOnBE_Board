package semicolon.MeetOn_Board.domain.file.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.MeetOn_Board.domain.file.domain.File;

import java.util.ArrayList;
import java.util.List;

public class FileDto {

    @Getter
    @NoArgsConstructor
    public static class FileResponseDtoList {
        private List<FileResponseDto> fileList = new ArrayList<>();

        @Builder
        public FileResponseDtoList(List<FileResponseDto> fileList) {
            this.fileList = fileList;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class FileResponseDto {
        private String fileUrl;

        @Builder
        public FileResponseDto(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public static FileResponseDto of(File file) {
            return FileResponseDto
                    .builder()
                    .fileUrl(file.getUrl())
                    .build();
        }
    }
}
