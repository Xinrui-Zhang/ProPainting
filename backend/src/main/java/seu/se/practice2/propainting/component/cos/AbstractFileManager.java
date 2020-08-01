package seu.se.practice2.propainting.component.cos;

import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Objects;

public abstract class AbstractFileManager
    implements MultipartFileAndFileTransfer, FileUploader, FileNameTypeManager {

    public static final String POINT = ".";

    public String doUploadMultipartFile(
        MultipartFile multipartFile, String... subDirs
    ) throws IOException {
        // Step1: 构建文件的名称
        String newFileNameWithoutType = generateFileNameWithoutType();
        // Step2: 构建文件的全称
        String newFileNameWithType = newFileNameWithoutType + POINT + getFileType(multipartFile);
        // Step3: 转换 MultipartFile 为本地 File 对象, 会生成新的文件
        File targetFile = fromMultipartFileToFile(multipartFile, newFileNameWithType);
        // Step4: 上传并且完成删除
        return uploadAndDeleteFile(targetFile, subDirs);
    }

    public String doUploadURLFile(String surl, String... subDirs) throws IOException {
        // Step1: 获取到原始图片的输入流
        URL url = new URL(surl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();
        BufferedImage read = ImageIO.read(inputStream);
        // Step2: 完成图片上传
        return doUploadBufferedImage(read, surl, subDirs);
    }

    public String doUploadBufferedImage(BufferedImage bi, String surl, String... subDirs) throws IOException {
        // Step1: 构建文件的名称
        String newFileNameWithoutType = generateFileNameWithoutType(bi);
        // Step2: 构建文件的全称
        String newFileNameWithType = newFileNameWithoutType + POINT + getFileType(surl);
        // Step3: 转换 MultipartFile 为本地 File 对象, 会生成新的文件
        File targetFile = new File(newFileNameWithType);
        InputStream inputStream = fromBufferedImageToInputStream(bi, getFileType(surl));
        FileUtils.copyInputStreamToFile(inputStream, targetFile);
        // Step4: 上传并且完成删除
        return uploadAndDeleteFile(targetFile, subDirs);
    }

    protected InputStream fromBufferedImageToInputStream(BufferedImage bi, String fileType) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, fileType, os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    @Override
    public String getFileType(MultipartFile multipartFile) {
        return Objects.requireNonNull(
            multipartFile.getOriginalFilename()
        ).substring(
            multipartFile.getOriginalFilename().lastIndexOf(POINT) + 1
        );
    }

    @Override
    public String getFileType(File file) {
        return file.getName().substring(file.getName().lastIndexOf(POINT) + 1);
    }

    @Override
    public File fromMultipartFileToFile(
        MultipartFile multipartFile, String newFileName
    ) throws IOException {
        if (newFileName == null || !newFileName.contains(POINT)) {
            // 获取到原始文件名
            String originalFileName = multipartFile.getOriginalFilename();
            if (originalFileName == null) {
                throw new IOException("多媒体文件转换过程中, 发现源文件名缺失!");
            } else {
                newFileName = originalFileName;
            }
        }
        // 配置结果
        File ansFile = new File(newFileName);
        // 使用 commons-io
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), ansFile);
        return ansFile;
    }

    @Override
    public File fromMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        return fromMultipartFileToFile(multipartFile, multipartFile.getOriginalFilename());
    }

    @Override
    public String getFileNameWithType(File file) {
        return file.getName();
    }

    @Override
    public String getFileNameWithoutType(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf(POINT));
    }

    @Override
    public MultipartFile fromFileToMultipartFile(
        File file, String newMultipartFileName
    ) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        return new MockMultipartFile(
            newMultipartFileName, file.getName(),
            ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream
        );
    }

    @Override
    public MultipartFile fromFileToMultipartFile(File file) throws IOException {
        return fromFileToMultipartFile(file, file.getName());
    }

    @Override
    public boolean deleteFileUnderProjectDir(String fileName) {
        // 然后应该删除项目目录下的本地文件
        File targetFile =
            new File(System.getProperty("user.dir") + File.separator + fileName);
        return targetFile.delete();
    }
}
