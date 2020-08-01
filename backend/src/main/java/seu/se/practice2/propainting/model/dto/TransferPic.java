package seu.se.practice2.propainting.model.dto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class TransferPic {
    private Integer index = null;
    private MultipartFile multipartFile = null;

    public String getModelName() throws ClientException {
        String model =  "";
        // 1为莫奈转照片，2为照片转莫奈，3、4为梵高<->照片，5、6为浮世绘<->照片，7、8为塞尚<->照片
        switch(index){
            case 1:
                return "monet2photo_pretrained";
            case 2:
                return "style_monet_pretrained";
            case 3://待自己训练
                return "monet2photo_pretrained";
            case 4:
                return "style_vangogh_pretrained";
            case 5:
                return "monet2photo_pretrained";
            case 6:
                return "style_ukiyoe_pretrained";
            case 7:
                return "monet2photo_pretrained";
            case 8:
                return "style_cezanne_pretrained";
            default:
                throw new ClientException("无效模型！");                               
        }
    }
    public String savePicFile(String timeStamp) throws ClientException, ServiceException {
        final String IMG_FOLDER_PREFIX = "style-transfer/pytorch-CycleGAN-and-pix2pix/imgs/";
        if(multipartFile==null){
            throw new ClientException("未选择文件！");
        }
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            throw new ServiceException("图片接收出错！", e);
        }
        if (bufferedImage == null) {// 证明上传的文件不是图片，获取图片流失败，不进行下面的操作
            throw new ClientException("文件格式不符！请重新上传。");
        }
        String contentType=multipartFile.getContentType();
        String format = contentType.substring(contentType.lastIndexOf("/")+1);
        String indirName = System.getProperty("user.dir") +"/python/"+IMG_FOLDER_PREFIX+"in/"+timeStamp;
        String outdirName = System.getProperty("user.dir") +"/python/"+IMG_FOLDER_PREFIX+"out/"+timeStamp;
        String fileName = indirName+"/"+timeStamp+"."+format;
        File indir = new File(indirName.replace('/', File.separatorChar));
        File outdir = new File(outdirName.replace('/', File.separatorChar));
        if(!indir.exists()){
            indir.mkdirs();
        }
        if(!outdir.exists()){
            outdir.mkdirs();
        }
        File ansFile = new File(fileName.replace('/', File.separatorChar));
        try {
            InputStream is = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(is, ansFile);
            is.close();
        } catch (IOException e) {
            throw new ServiceException("输入流转文件出错！",e);
        }
        if(ansFile.length()>512000){
            throw new ClientException("图片过大，请上传不大于500KB的图片！");
        }
        
        return format;
    }
    
    public byte[] getAndDeleteResult(String timeStamp,String format) throws ServiceException {
        final String IMG_FOLDER_PREFIX = System.getProperty("user.dir") +"/python/style-transfer/pytorch-CycleGAN-and-pix2pix/imgs/";
        String filePath= IMG_FOLDER_PREFIX+"out/"+timeStamp+"/"+timeStamp+"_fake.png";
        File  file = new File(filePath.replace('/', File.separatorChar));
        byte[] bytes;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            inputStream.close();
            String fInPath = IMG_FOLDER_PREFIX+"in/"+timeStamp+"/";
            String fOutPath = IMG_FOLDER_PREFIX+"out/"+timeStamp+"/";
            File fInDir = new File(fInPath);FileUtils.deleteDirectory(fInDir);
            File fOutDir = new File(fOutPath);FileUtils.deleteDirectory(fOutDir);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("返回效果图失败！");
        }
        return bytes;
    }
}