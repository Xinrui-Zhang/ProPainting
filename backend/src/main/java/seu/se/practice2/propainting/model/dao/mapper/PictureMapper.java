package seu.se.practice2.propainting.model.dao.mapper;


import java.util.List;
import java.util.Map;

//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import seu.se.practice2.propainting.model.dao.entity.Picture;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PictureMapper extends Mapper<Picture> {
    @Delete("delete from picture where pid=${pid}")
    int deletePic(Integer pid);

    @Update("IF ${like}==0 \n" +
            "\tupdate picture set picture.like=picture.like+1 where pid=${pid} \n" +
            "ELSE \n" +
            "\tupdate picture set picture.like=picture.like-1 where pid=${pid} \n" +
            "END IF;\n")
    int updateLike(Integer like,Integer pid);

    @Update("update picture set visibility='${visibility}' where pid=${pid}")
    int updateVisibiltyP(Integer pid,String visibility);

    
    @Select("SELECT pid,url,width,height FROM picture WHERE type='1' AND CONCAT(`title`,`titlezh`,`artistzh`,`artist`,`introzh`,`intro`,`tag`) "+
    "LIKE '%${q}%' AND genre LIKE '%${genre}%' AND tech LIKE '%${tech}%' AND style LIKE '%${style}%' AND visibility='1' AND state!='4'")
    List<Map<String, Object>> selectMasterOutline(String q,  String genre, String tech,String style);

    @Select("SELECT r1.pid,url,width,height "+
    "FROM "+
    "(SELECT pid,url,width,height FROM picture WHERE type='1' AND genre LIKE '%${genre}%' AND tech LIKE '%${tech}%' AND style LIKE '%${style}%' AND visibility='1' AND state!='4') AS r1 "+
    "JOIN "+
    "(SELECT CEIL(RAND() * (SELECT MAX(pid) FROM picture)) AS pid) AS r2 "+
    "WHERE r1.pid >= r2.pid ORDER BY r1.pid ASC")
    List<Map<String, Object>> selectRandomMasterOutline(String genre, String tech,String style);

    @Select("SELECT pid,url,width,height FROM picture WHERE type='2' AND CONCAT(`title`,`titlezh`,`artistzh`,`artist`,`introzh`,`intro`,`tag`) "+
    "LIKE '%${q}%' AND visibility='1' AND state!='4' ")
    List<Map<String, Object>> selectReferOutline(String q);
    
    @Select("SELECT * FROM("+
    "(SELECT * FROM picture WHERE pid=#{pid} AND visibility='1') t1,"+
    "(SELECT IF(count(*)>0,false,true) as liked FROM likemap WHERE uid=#{uid} AND type='1' AND id=#{pid}) t2,"+
    "(SELECT count(*) as collect FROM collectmap WHERE type='1' AND id=#{pid}) t3)")
    List<Map<String, Object>> selectPictureDetail(Integer pid,Long uid);

    @Select("SELECT pid,url,width,height FROM picture JOIN recommender ON " +
            "recommender.id = picture.pid AND recommender.uid=${uid} AND recommender.type='1'")
    List<Map<String,Object>> getRecommend(Long uid);

    @Select("SELECT url FROM"+
    "(SELECT url,SUBSTRING_INDEX(SUBSTRING_INDEX(resolution,',',1),'(',-1) AS width "+
    "FROM fileinfo WHERE pid=#{pid} ORDER BY width+'0' ASC LIMIT 1) t")
    String getSmallestResuluUrl(Integer pid);



}