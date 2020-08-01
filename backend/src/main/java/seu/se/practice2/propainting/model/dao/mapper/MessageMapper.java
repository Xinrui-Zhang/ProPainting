package seu.se.practice2.propainting.model.dao.mapper;

import org.springframework.stereotype.Repository;
import seu.se.practice2.propainting.model.dao.entity.Message;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface MessageMapper extends Mapper<Message> {

}
