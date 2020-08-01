package seu.se.practice2.propainting.model.dao.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import seu.se.practice2.propainting.model.dao.entity.Likemap;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface LikemapMapper extends Mapper<Likemap> {

    @Select("select isnull(type) from likemap where id=${pid} AND uid=${uid} ")
    String isnullPid(Integer pid,Long uid);
}
