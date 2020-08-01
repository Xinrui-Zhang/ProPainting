package seu.se.practice2.propainting.component.cos;

import java.io.File;
import java.io.IOException;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.error.ServiceException;

public class COSManager {
    private static final String secretId = "AKIDILbbKoF1kCOO8c9rc70cHlz0mJZY6p8A";
    private static final String secretKey = "xf8KFtLXR8ZaKoa6NPkgMoztXtymKEvL";
    private static final String bucketName = "propainting-1301790570";

    private static Region region = new Region("ap-nanjing");
    private static COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

    private static ClientConfig clientConfig = new ClientConfig(region);

    public void uploadFile(MultipartFile multipartFile, String fileName) throws ServiceException {
        File ansFile = new File(System.getProperty("user.dir") + File.separator+fileName.replace('/', File.separatorChar));
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), ansFile);
        } catch (IOException e) {
            throw new ServiceException("输入流转文件出错！",e);
        }

        COSClient cosClient = new COSClient(cred, clientConfig);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,ansFile);
        cosClient.putObject(putObjectRequest);
        cosClient.shutdown();        
    }

    public void uploadFileAndDeleteTemp(MultipartFile multipartFile,String fileName) throws ServiceException {
        uploadFile(multipartFile, fileName);
        File ansFile = new File(System.getProperty("user.dir") + File.separator+fileName.replace('/', File.separatorChar));
        ansFile.delete();
    }

    public void deleteFile(String fileName){
        COSClient cosClient = new COSClient(cred, clientConfig);
        cosClient.deleteObject(bucketName, fileName);
        cosClient.shutdown(); 
    }
}