package seu.se.practice2.propainting.model.dao.mapper;

import org.springframework.stereotype.Repository;
import seu.se.practice2.propainting.model.dao.entity.Account;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AccountMapper extends Mapper<Account> {

}