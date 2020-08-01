package seu.se.practice2.propainting.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import seu.se.practice2.propainting.component.cos.COSManager;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dao.entity.Account;
import seu.se.practice2.propainting.model.dao.entity.Fileinfo;
import seu.se.practice2.propainting.model.dao.entity.Likemap;
import seu.se.practice2.propainting.model.dao.entity.Message;
import seu.se.practice2.propainting.model.dao.entity.Picture;
import seu.se.practice2.propainting.model.dao.entity.Sketchrecord;
import seu.se.practice2.propainting.model.dao.mapper.AccountMapper;
import seu.se.practice2.propainting.model.dao.mapper.CollectionMapper;
import seu.se.practice2.propainting.model.dao.mapper.FileinfoMapper;
import seu.se.practice2.propainting.model.dao.mapper.LikemapMapper;
import seu.se.practice2.propainting.model.dao.mapper.MessageMapper;
import seu.se.practice2.propainting.model.dao.mapper.PictureMapper;
import seu.se.practice2.propainting.model.dao.mapper.RecommenderMapper;
import seu.se.practice2.propainting.model.dao.mapper.SketchrecordMapper;
import seu.se.practice2.propainting.model.dto.PicInfoAndFile;
import seu.se.practice2.propainting.model.dto.TransferPic;
import seu.se.practice2.propainting.service.PictureService;
import seu.se.practice2.propainting.validation.NotNull;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Validated
@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    private PictureMapper pictureMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private FileinfoMapper fileinfoMapper;

    @Autowired
    private LikemapMapper likemapMapper;
    @Autowired
    private CollectionMapper collectionMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SketchrecordMapper sketchrecordMapper;
    @Autowired
    private RecommenderMapper recommenderMapper;

    private COSManager cosManager = new COSManager();

    private static final String folderPrefix = "picture/";
    private static final String IN_FOLDER_PREFIX = "/home/style-transfer/pytorch-CycleGAN-and-pix2pix/imgs/in/";
    private static final String OUT_FOLDER_PREFIX = "/home/style-transfer/pytorch-CycleGAN-and-pix2pix/imgs/out/";
    private static final String BASH_CMD = "bash";
    private static final String COLOR_CARD_SCRIPT ="docker exec -t anaconda3 /bin/bash -c 'source activate propaint;python /home/color_card.py -i %s'";

    private static final String TRANSFER_SCRIPT = "docker exec -t anaconda3 /bin/bash -c 'source activate propaint;"+
    "cd /home/style-transfer/pytorch-CycleGAN-and-pix2pix;"+
    "python test.py --dataroot %s --results_dir %s --name %s --model test --no_dropout --gpu_ids -1'";
    
    private String uploadPictureAndUpdateDB(MultipartFile multipartFile, Picture p) throws ClientException,
        ServiceException {
        Integer width = 0;
        Integer height = 0;
        Integer pid = p.getPid();
        // 通过MultipartFile得到InputStream，从而得到BufferedImage
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            throw new ServiceException("图片接收出错！", e);
        }
        if (bufferedImage == null) {// 证明上传的文件不是图片，获取图片流失败，不进行下面的操作
            throw new ClientException("文件格式不符！请重新上传。");
        }
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();

        String contentType = multipartFile.getContentType();
        String format = contentType.substring(contentType.lastIndexOf("/") + 1);

        String fileName = folderPrefix + p.getPid().toString() + "_" + width.toString() + "_" + height.toString() + "." + format;
        cosManager.uploadFileAndDeleteTemp(multipartFile, fileName);

        String resolution = "(" + width.toString() + ", " + height.toString() + ")";
        Fileinfo fi = new Fileinfo(pid, fileName, resolution,
            String.valueOf(multipartFile.getSize()), format);
        fileinfoMapper.insert(fi);
        p.setUrl(fileName);
        p.setWidth(width);
        p.setHeight(height);
        pictureMapper.updateByPrimaryKeySelective(p);
        return p.getUrl();
    }


    private void checkAdminAuthority(Long uid) throws ClientException {
        Account account = accountMapper.selectByPrimaryKey(uid);
        if (!account.getAuthority().equals(2)) {
            throw new ClientException("权限不足！");
        }
    }

    @Override
    public void updateLike(@NotNull Integer like, @NotNull Integer pid, @NotNull String type, @NotNull Long uid) {

        if (type.equals("1"))//作品
        {

            Picture picture = new Picture();
            picture.setPid(pid);
            System.out.println(pictureMapper.selectByPrimaryKey(picture).getLike() + "这是当前点赞数");
            if (like == 0)    //点赞作品
                picture.setLike(pictureMapper.selectByPrimaryKey(pid).getLike() + 1);
            else
                picture.setLike(pictureMapper.selectByPrimaryKey(pid).getLike() - 1);

            Integer rcvid = pictureMapper.selectByPrimaryKey(picture).getAid();  //接收者ID
            String senname = accountMapper.selectByPrimaryKey(uid).getName();   //发送者名字
            String rcvname = accountMapper.selectByPrimaryKey(rcvid).getName();  //接收者名字
            String senavatar = accountMapper.selectByPrimaryKey(uid).getAvatar();
            String rcvavatar = accountMapper.selectByPrimaryKey(rcvid).getAvatar();

            //插入likemap
            Likemap likemap = new Likemap();
            likemap.setUid(Math.toIntExact(uid));
            likemap.setType(type);
            likemap.setId(rcvid);
            likemapMapper.insert(likemap);

            //发送一条消息
            Message message = new Message();
            message.setSenid(Math.toIntExact(uid));
            message.setRcvid(rcvid);
            message.setSenname(senname);
            message.setRcvname(rcvname);
            message.setSenavatar(senavatar);
            message.setRcvavatar(rcvavatar);
            if (like == 0)
                message.setContent("作品获得一条点赞");
            else
                message.setContent("用户取消作品点赞");
            message.setStime(new Date());
            message.setState("1");  //未读
            messageMapper.insert(message);
            pictureMapper.updateByPrimaryKeySelective(picture);
        } else {

            Integer rcvid = collectionMapper.selectByPrimaryKey(pid).getUid();  //接收者ID
            String senname = accountMapper.selectByPrimaryKey(uid).getName();   //发送者名字
            String rcvname = accountMapper.selectByPrimaryKey(rcvid).getName();  //接收者名字
            String senavatar = accountMapper.selectByPrimaryKey(uid).getAvatar();
            String rcvavatar = accountMapper.selectByPrimaryKey(rcvid).getAvatar();

            //插入likemap
            Likemap likemap = new Likemap();
            likemap.setUid(Math.toIntExact(uid));
            likemap.setType(type);
            likemap.setId(rcvid);
            likemapMapper.insert(likemap);

            //发送一条消息
            Message message = new Message();
            message.setSenid(Math.toIntExact(uid));
            message.setRcvid(rcvid);
            message.setSenname(senname);
            message.setRcvname(rcvname);
            message.setSenavatar(senavatar);
            message.setRcvavatar(rcvavatar);
            if (like == 0)
                message.setContent("收藏获得一条点赞");
            else
                message.setContent("用户取消收藏点赞");
            message.setStime(new Date());
            message.setState("1");  //未读
            messageMapper.insert(message);
        }

    }

    @Override
    public void addPicture(@NotNull PicInfoAndFile picInfoAndFile, String state, @NotNull Long uid
    ) throws ClientException, ServiceException {
        Picture p = picInfoAndFile.getPicture();
        if (state.equals("1")) {
            checkAdminAuthority(uid);
        }
        MultipartFile mf = picInfoAndFile.getMultipartFile();
        if (mf == null) {
            throw new ClientException("未选择文件！");
        }
        p.setState(state);
        p.setPid(null);//防止主键重复
        pictureMapper.insertSelective(p);
        uploadPictureAndUpdateDB(mf, p);
    }

    @Override
    public void deletePicture(@NotNull Integer pid, @NotNull Long uid
    ) throws ServiceException, ClientException {
        checkAdminAuthority(uid);
        Picture p = new Picture();
        p.setPid(pid);
        p.setState("4");
        pictureMapper.updateByPrimaryKeySelective(p);
    }

    @Override
    public void updatePicture(@NotNull PicInfoAndFile picInfoAndFile, @NotNull Long uid
    ) throws ServiceException, ClientException {
        checkAdminAuthority(uid);

        Picture p = picInfoAndFile.getPicture();
        MultipartFile mf = picInfoAndFile.getMultipartFile();
        Picture raw = pictureMapper.selectByPrimaryKey(p.getPid());
        if (raw == null) {
            throw new ClientException("无效图片id！");
        }
        if (mf == null) {
            pictureMapper.updateByPrimaryKeySelective(p);
        } else {
            cosManager.deleteFile(raw.getUrl());
            Fileinfo fi = new Fileinfo();
            fi.setPid(p.getPid());
            fileinfoMapper.deleteByExample(Example.builder(Fileinfo.class)
                .where(Sqls.custom().andEqualTo("pid", fi.getPid())).build());

            String newUrl = uploadPictureAndUpdateDB(mf, p);
            System.out.println(newUrl);
            Sketchrecord sr = new Sketchrecord();
            sr.setPurl(newUrl);
            sketchrecordMapper.updateByExampleSelective(sr, Example.builder(Sketchrecord.class)
                .where(Sqls.custom().andEqualTo("pid", p.getPid())).build());
            System.out.println("sketch");
        }
    }

    @Override
    public void updateVisibiltyP(@NotNull Integer pid, @NotNull String visibility) {
        Picture picture = new Picture();
        picture.setPid(pid);
        picture.setVisibility(visibility);
        pictureMapper.updateByPrimaryKeySelective(picture);
    }

    @Override
    public Map getPicture(@NotNull Integer pid, @NotNull Long uid) throws ServiceException, ClientException {
        Picture picture = new Picture();
        picture.setPid(pid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("picture", pictureMapper.selectOne(picture));
        Integer liked = 0;
        Likemap likemap = new Likemap();
        likemap.setUid(Math.toIntExact(uid));
        likemap.setId(pid);
        likemap.setType("1");
        System.out.println(likemapMapper.selectCount(likemap));
        if (likemapMapper.selectCount(likemap) > 0)
            liked = 0;
        else
            liked = 1;
        map.put("liked", liked);
        return map;
    }

    @Override
    public Object getColorCard(@NotNull Integer pid
    ) throws ServiceException, ClientException {
        Picture p = pictureMapper.selectByPrimaryKey(pid);
        if (p == null)
            throw new ClientException("图片id无效！");
        else if (!p.getColors().equals(""))
            return p.getColors();
        else {
            // String url = pictureMapper.getSmallestResuluUrl(pid);
            String url = p.getUrl();
            if (url == null)
                throw new ServiceException("图片文件信息不全，请提交争议请求！");
            StringBuilder colors = new StringBuilder();
            try{              
                ProcessBuilder pb = new ProcessBuilder(BASH_CMD,"-c",String.format(COLOR_CARD_SCRIPT,url));
                pb.redirectErrorStream(true);
                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                
                String line;
                while ((line = reader.readLine()) != null)
                    colors.append(line);
                process.waitFor();
                // System.out.println(colors.toString());
                if(colors.toString().length()>90){
                    throw new ServiceException("生成色卡繁忙，请稍后重试！");
                }
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }
            Picture newp = new Picture();
            newp.setPid(pid);
            newp.setColors(colors.toString());
            pictureMapper.updateByPrimaryKeySelective(newp);
            return colors.toString();
        }
    }

    @Override
    public Object transferPic(@NotNull TransferPic transferPic) throws ClientException, ServiceException {
        String model = transferPic.getModelName();
        // 获取当前时间戳作为文件名
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStamp=simpleDateFormat.format(new Date());
        String format = transferPic.savePicFile(timeStamp);
        
        // 调用python预测脚本
        String dataroot = IN_FOLDER_PREFIX.replace('/', File.separatorChar)+timeStamp;
        String result_dir = OUT_FOLDER_PREFIX.replace('/', File.separatorChar)+timeStamp;
        String result_code="";
        StringBuilder cause = new StringBuilder();
        try{
            ProcessBuilder pb = new ProcessBuilder(BASH_CMD,"-c",String.format(TRANSFER_SCRIPT,dataroot,result_dir,model));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            
            while ((line = reader.readLine()) != null){
                result_code = line;
                cause.append(line);
            }
                
            process.waitFor();
        }
        catch(Exception e){
            e.printStackTrace();
            throw new ServiceException("执行风格迁移脚本失败！");
        }
        if(!result_code.startsWith("1")){
            // System.out.println(result_code);
            throw new ServiceException("图片风格迁移失败！"+result_code+"\n"+cause.toString());
        }
        //返回效果图
        return transferPic.getAndDeleteResult(timeStamp, format);
        

    }
}
