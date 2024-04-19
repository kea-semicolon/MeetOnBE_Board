package semicolon.MeetOn_Board.domain.file.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Board.domain.file.dao.FileRepository;
import semicolon.MeetOn_Board.domain.file.domain.File;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;

    public List<File> boardFileList(Long boardId) {
        return fileRepository.findAllByBoardId(boardId);
    }

    public void deleteFile(Long boardId) {
        fileRepository.deleteAllByBoardId(boardId);
    }
}
