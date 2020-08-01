package seu.se.practice2.propainting.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dao.entity.Message;
import seu.se.practice2.propainting.model.dao.mapper.MessageMapper;
import seu.se.practice2.propainting.service.MessageService;
import seu.se.practice2.propainting.validation.NotNull;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Message getMessage(@NotNull Integer mid) throws ServiceException, ClientException {
        Message message=new Message();
        message.setMid(mid);
        message.setState("2");
        messageMapper.updateByPrimaryKeySelective(message);
        return messageMapper.selectByPrimaryKey(message);
    }

    @Override
    public List<Map<String, Object>> userMessage(@NotNull Long uid) throws ServiceException, ClientException {
        Message message = new Message();
        message.setRcvid(Math.toIntExact(uid));
        List<Map<String, Object>> list = new LinkedList<>();
        for (Message value : messageMapper.select(message)) {
            Map<String, Object> map = new HashMap<>();
            map.put("content", value.getContent());
            map.put("state", value.getState());
            map.put("mid", value.getMid());
            list.add(map);
        }

        return list;
    }
}
