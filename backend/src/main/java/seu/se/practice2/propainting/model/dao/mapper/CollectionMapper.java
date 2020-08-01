package seu.se.practice2.propainting.model.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import seu.se.practice2.propainting.model.dao.entity.Collect;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CollectionMapper extends Mapper<Collect> {

    @Update("update collection set visibility='${visibility}' where cid=${cid}")
    int updateVisibiltyS(Integer cid,String visibility);
    
    @Select("SELECT f.cid, f.title,"+
    "GROUP_CONCAT(f.url SEPARATOR '|') as urls,"+
    "GROUP_CONCAT(f.width SEPARATOR '|') as widths,"+
    "GROUP_CONCAT(f.height SEPARATOR '|') as heights "+
    "FROM "+
    "(SELECT collection.title,t1.* "+
    "from collection "+
    "join "+
    "(SELECT cid,pid,url,width,height FROM( "+
    "SELECT cid,pid,url,width,height ,(@num:=if(@cid=`cid`,@num+1,if(@cid:=`cid`,1,1))) as rown "+
    "from picture "+
    "join collectmap "+
    "on picture.pid = collectmap.id "+
    "and collectmap.type='1' "+
    "cross join (select @num:=0, @cid:=0) c) as x "+
    "where x.rown<=4) t1 "+
    "on collection.cid=t1.cid "+
    "and collection.title like '%${q}%' AND visibility='1' AND state!='4' "+
    "order by cid,pid asc) f "+
    "group by f.cid")
    List<Map<String, Object>> selectCollectionOutline(String q);
    
    @Select("SELECT * FROM("+
    "(SELECT * FROM collection WHERE cid=#{cid} AND visibility='1') t1,"+
    "(SELECT IF(count(*)>0,false,true) as collected FROM collectmap WHERE cid=(SELECT cid FROM collection WHERE uid=#{uid} AND title='收藏的资源集') AND type='2' AND id=#{cid}) t2,"+
    "(SELECT IF(count(*)>0,false,true) as liked FROM likemap WHERE uid=#{uid} AND type='2' AND id=#{cid}) t3,"+
    "(SELECT count(*) AS collect FROM collectmap WHERE type='2' AND id=#{cid}) t4)")
    List<Map<String, Object>> selectCollectionDetail(Integer cid,Long uid);


    @Select("SELECT pid,url,width,height FROM collectmap "+
    "JOIN picture "+
    "ON collectmap.id = picture.pid "+
    "AND collectmap.cid=#{cid} "+
    "AND collectmap.type='1'")
    List<Map<String, Object>> selectPictureList(Integer cid);



    @Select("SELECT f.cid, f.title,"+
    "GROUP_CONCAT(f.url SEPARATOR '|') as urls,"+
    "GROUP_CONCAT(f.width SEPARATOR '|') as widths,"+
    "GROUP_CONCAT(f.height SEPARATOR '|') as heights "+
    "FROM "+
    "(SELECT collection.title,t1.* "+
    "from collection "+
    "join "+
    "(SELECT cid,pid,url,width,height FROM("+
    "SELECT cid,pid,url,width,height ,(@num:=if(@cid=`cid`,@num+1,if(@cid:=`cid`,1,1))) as rown "+
    "from picture "+
    "join collectmap "+
    "on picture.pid = collectmap.id "+
    "and collectmap.type='1' "+
    "cross join (select @num:=0, @cid:=0) c) as x "+
    "where x.rown<=4) t1 "+
    "on collection.cid=t1.cid "+
    "and collection.cid IN "+
    "(SELECT id FROM collectmap "+ 
    "JOIN collection "+
    "ON collection.cid = collectmap.cid "+
    "AND collection.title='收藏的资源集' "+
    "AND collection.uid = #{uid} "+
    "AND collectmap.type='2') "+
    "order by cid,pid asc) f "+
    "group by f.cid") 
    List<Map<String, Object>> selectCollectionList(Long uid);





}