package seu.se.practice2.propainting.service.impl;

import com.github.pagehelper.PageHelper;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dao.entity.Picture;
import seu.se.practice2.propainting.model.dao.entity.Sketchrecord;
import seu.se.practice2.propainting.model.dao.mapper.PictureMapper;
import seu.se.practice2.propainting.model.dao.mapper.SketchrecordMapper;
import seu.se.practice2.propainting.model.dto.PageResult;
import seu.se.practice2.propainting.model.dto.ResultData;
import seu.se.practice2.propainting.service.PictureService;
import seu.se.practice2.propainting.service.SketchrecordService;
@Service
public class SketchrecordServiceImpl implements SketchrecordService {
    @Autowired
    private SketchrecordMapper sketchrecordMapper;
    @Autowired
    private PictureMapper pictureMapper;

    @Override
    public PageResult checkRecord(@Range(min = 0) Integer pageIndex, @Range(min = 0) Integer pageSize,Long uid) throws ServiceException, ClientException {
        Sketchrecord sketchrecord=new Sketchrecord();
        sketchrecord.setUid(Math.toIntExact(uid));

        PageHelper.startPage(pageIndex, pageSize);
        return PageResult.wrap(sketchrecordMapper.select(sketchrecord));
    }

    @Override
    public ResultData addRecord(Integer[] pids, Long uid) throws ServiceException, ClientException {
        int length=pids.length;
        Sketchrecord sketchrecord=new Sketchrecord();
        sketchrecord.setUid(Math.toIntExact(uid));
        Picture picture=new Picture();
        for(int i=0;i<length-1;i++)
        {
            sketchrecord.setPid(pids[i]);
            picture.setPid(pids[i]);
            sketchrecord.setPurl(pictureMapper.selectOne(picture).getUrl());
            sketchrecordMapper.insertSelective(sketchrecord);
        }
        sketchrecord.setPid(pids[length-1]);

        return ResultData.wrap(sketchrecordMapper.insertSelective(sketchrecord));
    }
}
