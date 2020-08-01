package seu.se.practice2.propainting.config.thread_pool;

import seu.se.practice2.propainting.component.cos.COSFileManager;
import seu.se.practice2.propainting.model.dao.entity.Picture;
import seu.se.practice2.propainting.model.dao.mapper.PictureMapper;

import java.io.IOException;
import java.util.concurrent.RecursiveTask;

public class ImageTransRecrusiveTask extends RecursiveTask<String> {

    private final COSFileManager fileManager;

    private final String surl;

    private final PictureMapper pictureMapper;

    private final Long pid;

    public ImageTransRecrusiveTask(
        COSFileManager abstractFileManager, String surl, PictureMapper pictureMapper, Long pid) {
        this.fileManager = abstractFileManager;
        this.surl = surl;
        this.pictureMapper = pictureMapper;
        this.pid = pid;
    }

    @Override
    protected String compute() {
        String ans = "";
        try {
            fileManager.setPid(pid.toString());
            ans = fileManager.doUploadURLFile(surl, "picture");
            Picture picture = new Picture();
            picture.setPid(Integer.parseInt(pid.toString()));
            picture.setUrl("picture/" + ans.substring(ans.lastIndexOf("/") + 1));
            pictureMapper.updateByPrimaryKeySelective(picture);
        } catch (IOException ioException) {
            System.out.println("异常 url: " + surl);
        }
        return ans;
    }
}
