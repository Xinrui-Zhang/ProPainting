package seu.se.practice2.propainting.service;

import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dao.entity.Message;
import seu.se.practice2.propainting.model.dto.ResultData;
import seu.se.practice2.propainting.validation.NotNull;

import java.util.List;
import java.util.Map;

@Validated
public interface MessageService {

    Message getMessage(@NotNull Integer mid)throws ServiceException, ClientException;
    List<Map<String, Object>> userMessage(@NotNull Long uid)throws ServiceException, ClientException;
}
