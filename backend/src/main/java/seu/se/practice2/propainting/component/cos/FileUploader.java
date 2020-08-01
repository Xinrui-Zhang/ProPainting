package seu.se.practice2.propainting.component.cos;

import java.io.File;

public interface FileUploader {

	String uploadFile(File file, String... subDirs);

	String uploadAndDeleteFile(File file, String... subDirs);
}
