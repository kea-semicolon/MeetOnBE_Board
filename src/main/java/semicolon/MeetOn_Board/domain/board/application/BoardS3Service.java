package semicolon.MeetOn_Board.domain.board.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semicolon.MeetOn_Board.domain.file.domain.File;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardS3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile, Long boardId, int i) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        String saveName = "Board " + boardId + "-file" + i + "." + type;
        amazonS3.putObject(bucket, saveName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, saveName).toString();
    }

    public List<String> saveFiles(List<MultipartFile> multipartFiles, Long boardId) throws IOException {
        List<String> result = new ArrayList<>();
        for (int i=0; i<multipartFiles.size(); i++) {
            result.add(saveFile(multipartFiles.get(i), boardId, i));
        }
        return result;
    }

    public void deleteFiles(List<File> fileList) {
        List<DeleteObjectsRequest.KeyVersion> target = fileList.stream()
                .map(File::getUrl)
                .map(BoardS3Service::extractPath)
                .map(DeleteObjectsRequest.KeyVersion::new)
                .toList();
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(target);
        amazonS3.deleteObjects(deleteObjectsRequest);
    }

    private static String extractPath(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getPath().substring(1); // Remove the leading '/'
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
