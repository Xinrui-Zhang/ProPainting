package seu.se.practice2.propainting.model.dao.mapper;

import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;
import seu.se.practice2.propainting.model.dao.entity.Recommender;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

@Repository
public interface RecommenderMapper extends Mapper<Recommender> {
    @Delete("DELETE FROM recommender where uid =${uid} AND DATEDIFF('${time}',recommender.time)>10")
    int removeOverTime(Long uid, Date time);

}
