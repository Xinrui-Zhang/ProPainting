package seu.se.practice2.propainting.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.model.dao.entity.Picture;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class PicInfoAndFile {
    private Integer pid = null;
    private String type = null;
    private String titlezh = null;
    private String title = null;
    private String artistzh = null;
    private String artist = null;
    private String avatar = null;
    private String genre = null;
    private String tech = null;
    private String style = null;
    private String introzh = null;
    private String intro = null;
    private String visibility = null;
    private String tag = null;
    private String refer = null;
    private MultipartFile multipartFile = null;



    public Picture getPicture() throws ClientException {
        allCharacterValid();
        Picture p = new Picture(pid,type,titlezh,title,null,null,null,artistzh,artist,
        null,avatar,genre,tech,style,introzh,intro,null,visibility,tag,refer,null,null);
        return p;
    }

    public void allCharacterValid() throws ClientException {
        final String VALID_TYPE="123";
        final String VALID_VISIBILITY="01";
        if(type!=null && !VALID_TYPE.contains(type)){
            throw new ClientException("无效图片类型！");
        }
        if(visibility!=null && !VALID_VISIBILITY.contains(visibility)){
            throw new ClientException("无效可见性！");
        }


    }
}