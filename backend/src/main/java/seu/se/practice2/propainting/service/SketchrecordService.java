package seu.se.practice2.propainting.service;

import org.hibernate.validator.constraints.Range;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dto.PageResult;
import seu.se.practice2.propainting.model.dto.ResultData;

public interface SketchrecordService {
    PageResult checkRecord(
            @Range(min = 0) Integer pageIndex, @Range(min = 0) Integer pageSize,Long uid
    )throws ServiceException, ClientException;

    ResultData addRecord(
            Integer[] pids,Long uid
    )throws ServiceException, ClientException;
}
