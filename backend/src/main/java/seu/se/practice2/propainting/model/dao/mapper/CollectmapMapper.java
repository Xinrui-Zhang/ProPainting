package seu.se.practice2.propainting.model.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;
import seu.se.practice2.propainting.model.dao.entity.Collectmap;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CollectmapMapper extends Mapper<Collectmap> {

	@Insert("INSERT INTO collectmap(id,cid,type) values(${pid},${cid},'${type}')")
	void insertPictoCollect(Integer pid, Integer cid, String type);


}
