package seu.se.practice2.propainting.service;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dto.PageResult;

@Validated
public interface RecommenderService {

    void recommendPicture(Long uid)throws ServiceException, ClientException;
    boolean isLike(Long uid,Integer pid,String type);
    Object getRecommend(Long uid,@Range(min = 0) Integer pageIndex,
                            @Range(min = 0) Integer pageSize)throws ServiceException, ClientException;


}
