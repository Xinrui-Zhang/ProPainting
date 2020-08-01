package seu.se.practice2.propainting.model.dao.mapper;

import org.springframework.stereotype.Repository;
import seu.se.practice2.propainting.model.dao.entity.Request;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface RequestMapper extends Mapper<Request> {
}
