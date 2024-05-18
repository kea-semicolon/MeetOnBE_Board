package semicolon.MeetOn_Board.domain.file.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Board.domain.board.application.BoardS3Service;
import semicolon.MeetOn_Board.domain.file.dao.FileRepository;
import semicolon.MeetOn_Board.domain.file.domain.File;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    private final BoardS3Service deleteService;

    public List<File> boardFileList(Long boardId) {
        return fileRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public void deleteFile(Long boardId) {
        List<File> fileList = fileRepository.findAllByBoardId(boardId);
        if(fileList != null && !fileList.isEmpty()) {
            log.info("delete File");
            deleteService.deleteFiles(fileList);
        }
        fileRepository.deleteAllByBoardId(boardId);
    }
}
