package seu.se.practice2.propainting.component.cos;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface MultipartFileAndFileTransfer {

	File fromMultipartFileToFile(MultipartFile multipartFile, String newFileName)
		throws IOException;

	File fromMultipartFileToFile(MultipartFile multipartFile) throws IOException;

	MultipartFile fromFileToMultipartFile(File file, String newMultipartFileName)
		throws IOException;

	MultipartFile fromFileToMultipartFile(File file) throws IOException;

	boolean deleteFileUnderProjectDir(String fileName);
}
