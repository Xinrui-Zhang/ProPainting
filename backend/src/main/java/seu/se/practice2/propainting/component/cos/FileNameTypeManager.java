package seu.se.practice2.propainting.component.cos;

import java.awt.image.BufferedImage;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileNameTypeManager {

	String generateFileNameWithoutType();

	String getFileNameWithType(File file);

	String getFileNameWithoutType(File file);

	String getFileType(File file);

	String getFileType(String surl);

	String getFileType(MultipartFile multipartFile);

	String generateFileNameWithoutType(BufferedImage bi);

}
