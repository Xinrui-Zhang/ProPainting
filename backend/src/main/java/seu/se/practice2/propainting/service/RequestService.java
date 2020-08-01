package seu.se.practice2.propainting.service;

import org.hibernate.validator.constraints.Range;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dto.PageResult;
import seu.se.practice2.propainting.model.dto.ResultData;
import seu.se.practice2.propainting.validation.NotNull;

public interface RequestService {
    void postRequest(@NotNull String type,@NotNull Integer pid,@NotNull String content,@NotNull Long uid)throws ServiceException, ClientException;

    void dealRequest(@NotNull Integer rid,@NotNull String content,@NotNull Long uid)throws ServiceException, ClientException;

    Object checkRequest(@Range(min = 0) Integer pageIndex,@Range(min = 0) Integer pageSize)throws ServiceException, ClientException;
}
