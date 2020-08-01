package seu.se.practice2.propainting.component.cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import seu.se.practice2.propainting.component.id.IDGenerator;
import seu.se.practice2.propainting.config.cos.COSConfigurer.COSHolder;

import java.awt.image.BufferedImage;
import java.io.File;

@Component
public class COSFileManager extends AbstractFileManager {

	@Autowired
	private COSHolder cosHolder;

	@Autowired
	private IDGenerator idGenerator;

	private String pid;

	public void setPid(String _pid) {
		pid = _pid;
	}

	@Override
	public String generateFileNameWithoutType() {
		// 生成文件唯一主键key
		return String.valueOf(idGenerator.nextId());
	}

	@Override
	public String getFileType(String surl) {
		return surl.substring(surl.lastIndexOf(".") + 1);
	}

	@Override
	public String generateFileNameWithoutType(BufferedImage bi) {
		int w = bi.getWidth(), h = bi.getHeight();
		return String.format("%s_%d_%d", pid, w, h);
	}

	@Override
	public String uploadFile(File file, String... subDirs) {
		// 首先获取到url前缀
		String ansFileUrl = getBaseURL();
		// 创建新的文件路径
		StringBuilder bucketNameBuilder = new StringBuilder();
		for (String subDir : subDirs) {
			bucketNameBuilder.append("/").append(subDir);
		}
		String uploadFileKey =
			bucketNameBuilder.append("/").append(
				getFileNameWithType(file)
			).toString();
		// 上传文件
		// 创建客户端
		COSClient cosClient = cosHolder.newCOSClient();
		// 上传图片文件到指定的桶中
		PutObjectRequest putObjectRequest = new PutObjectRequest(
			cosHolder.getBucketName(),
			uploadFileKey,
			file
		);
		cosClient.putObject(putObjectRequest);
		// 关闭
		cosHolder.closeCOSClient(cosClient);
		return ansFileUrl + putObjectRequest.getKey();
	}

	@Override
	public String uploadAndDeleteFile(File file, String... subDirs) {
		String ans = uploadFile(file, subDirs);
		deleteFileUnderProjectDir(file.getName());
		return ans;
	}

	public String getBaseURL() {
		return cosHolder.getBaseUrl();
	}

}
