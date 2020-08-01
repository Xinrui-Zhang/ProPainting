package seu.se.practice2.propainting.service.impl;

import com.github.pagehelper.PageHelper;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dao.entity.Account;
import seu.se.practice2.propainting.model.dao.entity.Message;
import seu.se.practice2.propainting.model.dao.entity.Request;
import seu.se.practice2.propainting.model.dao.mapper.AccountMapper;
import seu.se.practice2.propainting.model.dao.mapper.MessageMapper;
import seu.se.practice2.propainting.model.dao.mapper.PictureMapper;
import seu.se.practice2.propainting.model.dao.mapper.RequestMapper;
import seu.se.practice2.propainting.model.dto.PageResult;
import seu.se.practice2.propainting.model.dto.ResultData;
import seu.se.practice2.propainting.service.PictureService;
import seu.se.practice2.propainting.service.RecommenderService;
import seu.se.practice2.propainting.service.RequestService;
import seu.se.practice2.propainting.validation.NotNull;

import java.util.*;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private PictureMapper pictureMapper;

    @Override
    public void postRequest(@NotNull String type,@NotNull Integer pid,@NotNull String content,@NotNull Long uid) throws ServiceException, ClientException {
        Request request=new Request();
        request.setPid(pid);
        request.setUid(Math.toIntExact(uid));
        request.setContent(content);
        request.setType(type);
        request.setAvatar(accountMapper.selectByPrimaryKey(uid).getAvatar());
        request.setName(accountMapper.selectByPrimaryKey(uid).getName());
        request.setResult(0);
        requestMapper.insert(request);
    }
    private void checkAdminAuthority(Long uid) throws ClientException {
        Account account = accountMapper.selectByPrimaryKey(uid);
        if(!account.getAuthority().equals(2)){
            throw new ClientException("权限不足！");
        }
    }

    @Override
    public void dealRequest(@NotNull Integer rid,@NotNull String content,@NotNull Long uid) throws ServiceException, ClientException {
        checkAdminAuthority(uid);
        Message message=new Message();
        message.setState("1");
        message.setContent(content);
        message.setSenid(Math.toIntExact(uid));
        message.setSenname(accountMapper.selectByPrimaryKey(uid).getName());  //发送者名字
        message.setSenavatar(accountMapper.selectByPrimaryKey(uid).getAvatar());
        message.setRcvid(requestMapper.selectByPrimaryKey(rid).getUid());
        message.setRcvname(requestMapper.selectByPrimaryKey(rid).getName());  //接收者名字，通过request表得到
        message.setRcvavatar(requestMapper.selectByPrimaryKey(rid).getAvatar());

        Request request=new Request();
        request.setRid(rid);
        request.setResult(Math.toIntExact(uid));   //处理结果为正数，为处理者ID
        requestMapper.updateByPrimaryKeySelective(request);
        messageMapper.insert(message);
    }

    @Override
    public Object checkRequest(@Range(min = 0)Integer pageIndex, @Range(min = 0) Integer pageSize) throws ServiceException, ClientException {
        Request request=new Request();
        request.setResult(0);
        List<Map<String,Object>> list = new LinkedList<>();

        PageHelper.startPage(pageIndex, pageSize);
        Iterator<Request> iterator = requestMapper.select(request).iterator();
        while (iterator.hasNext())
        {
            Request request1 = iterator.next();
            Integer pid = request1.getPid();
            String url = pictureMapper.selectByPrimaryKey(pid).getUrl();
            Map<String,Object> map = new HashMap<>();
            map.put("url",url);
            map.put("request",request1);
            list.add(map);
        }
        return list;
      //return PageResult.wrap(list);

    }

}
