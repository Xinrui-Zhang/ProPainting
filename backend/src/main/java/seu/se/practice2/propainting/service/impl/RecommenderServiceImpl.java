package seu.se.practice2.propainting.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dao.entity.Collect;
import seu.se.practice2.propainting.model.dao.entity.Collectmap;
import seu.se.practice2.propainting.model.dao.entity.Likemap;
import seu.se.practice2.propainting.model.dao.entity.Picture;
import seu.se.practice2.propainting.model.dao.entity.Recommender;
import seu.se.practice2.propainting.model.dao.mapper.CollectionMapper;
import seu.se.practice2.propainting.model.dao.mapper.CollectmapMapper;
import seu.se.practice2.propainting.model.dao.mapper.LikemapMapper;
import seu.se.practice2.propainting.model.dao.mapper.PictureMapper;
import seu.se.practice2.propainting.model.dao.mapper.RecommenderMapper;
import seu.se.practice2.propainting.model.dto.PageResult;
import seu.se.practice2.propainting.service.RecommenderService;
import seu.se.practice2.propainting.util.URLUtil;

@Service
public class RecommenderServiceImpl implements RecommenderService {
    @Autowired
    private RecommenderMapper recommenderMapper;
    @Autowired
    private CollectmapMapper collectmapMapper;
    @Autowired
    private CollectionMapper collectionMapper;
    @Autowired
    private PictureMapper pictureMapper;
    @Autowired
    private LikemapMapper likemapMapper;
    @Autowired
    URLUtil urlUtil;


    @Override
    public void recommendPicture(Long uid) throws ServiceException, ClientException {
        List<Picture> list= new LinkedList<>();
        recommenderMapper.removeOverTime(uid,new Date());
        //删除掉大于10天的推荐
        Recommender recommender =new Recommender();

        Likemap likemap =new Likemap();
        likemap.setUid(Math.toIntExact(uid));
        System.out.println("uid"+uid);
//        System.out.println(likemapMapper.select(likemap));
        Iterator<Likemap> iterator=likemapMapper.select(likemap).iterator();  //先看点赞的推荐
        while (iterator.hasNext())
        {
            Likemap likemapCur =iterator.next();
            Integer id=likemapCur.getId();
            System.out.println("点赞ID"+id);
            String type =likemapCur.getType();
            if(type.equals("1"))  //是一张图片，就找此图片映射到collectionmap中的其他图返回
            {
                Collectmap collectmap = new Collectmap();
                collectmap.setId(id);
                collectmap.setType(type);
                Iterator<Collectmap> iteratorMap = collectmapMapper.select(collectmap).iterator();
                while (iteratorMap.hasNext())
                {
                    Collectmap collectmapCur =iteratorMap.next();
                    Integer cid =collectmapCur.getCid();
                    Collect collect = collectionMapper.selectByPrimaryKey(cid);
                    if(collect.getUid()==Math.toIntExact(uid))  //要排除掉用户本人的收藏
                    {
                        continue;
                    }
                    //否则将此收藏里的所有图片作为智能推荐的图片
                    Collectmap collectmap1 = new Collectmap();
                    collectmap1.setCid(cid);
                  //  PageHelper.startPage(pageIndex, pageSize);
                    Iterator<Collectmap> iteratorCur = collectmapMapper.select(collectmap1).iterator();
                    while (iteratorCur.hasNext())
                    {
                        Picture picture = pictureMapper.selectByPrimaryKey(iteratorCur.next().getId());
                      if ( isLike(uid,picture.getPid(),"1"))     //没有点过赞
                         // list.add(picture);  //图片加入
                      {
                          recommender.setUid(uid);
                          recommender.setType("1");
                          recommender.setTime(new Date());
                          recommender.setId(picture.getPid());
                          recommenderMapper.insert(recommender);
                      }
                    }
                }
            }
            else   //是收藏集,可以返回此收藏集的其他图片
            {
                Collectmap collectmap = new Collectmap();
                collectmap.setCid(id);
                Iterator<Collectmap> iteratorCollect = collectmapMapper.select(collectmap).iterator();
                while (iteratorCollect.hasNext())
                {
                    Collectmap curMap =iteratorCollect.next();
                    Integer curId =curMap.getId();
                    String curType=curMap.getType();
                    if(curType.equals("1"))
                    {
                        Picture picture = pictureMapper.selectByPrimaryKey(curId);
                        if ( isLike(uid,picture.getPid(),"1"))     //没有点过赞
                        {      //list.add(picture);
                        recommender.setUid(uid);
                        recommender.setType("1");
                        recommender.setTime(new Date());
                        recommender.setId(picture.getPid());
                        recommenderMapper.insert(recommender);}
                    }
                }
            }
        }

        Collect collect = new Collect();
        collect.setUid(Math.toIntExact(uid));
        Integer cid =collectionMapper.selectOne(collect).getCid();
        //就以一个收藏夹的内容来做智能推荐（因为多个收藏夹是在图片太多了，影响运行速度)
        Collectmap collectmap = new Collectmap();
        collectmap.setCid(cid);
        Iterator<Collectmap> iterator1 = collectmapMapper.select(collectmap).iterator();
        while (iterator1.hasNext())
        {
            Collectmap collectmapCur = iterator1.next();
            Integer id =collectmapCur.getId();
            String type = collectmapCur.getType();
            if(type.equals("1"))
            {
                collectmap.setCid(id);
                Iterator<Collectmap> iterator2 = collectmapMapper.select(collectmap).iterator();
                while (iterator2.hasNext())
                {
                    Integer sameCid =iterator2.next().getCid();
                    Collectmap collectmap1 = new Collectmap();
                    collectmap1.setCid(sameCid);
                    Iterator<Collectmap> collectmapIterator = collectmapMapper.select(collectmap1).iterator();
                    while (collectmapIterator.hasNext())
                    {
                        Collectmap collectmap2 = collectmapIterator.next();
                        String types =collectmap2.getType();
                        Integer pid =collectmap2.getId();
                        if(types.equals("1"))
                        {
                            recommender.setUid(uid);
                            recommender.setType("1");
                            recommender.setTime(new Date());
                            recommender.setId(pid);
                            recommenderMapper.insert(recommender);
                        }

                    }

                }
            }
        }

    }

    @Override      //要排除掉用户已经点过赞的图片或集合作为推荐
    public boolean isLike(Long uid, Integer pid,String type) {
        Likemap likemap = new Likemap();
        likemap.setType(type);
        likemap.setId(pid);
        likemap.setUid(Math.toIntExact(uid));
        if(likemapMapper.select(likemap).isEmpty())
            return true;       //没有点赞过
        return false;
    }

    @Override
    public Object getRecommend(Long uid,@Range(min = 0) Integer pageIndex,
                                   @Range(min = 0) Integer pageSize) throws ServiceException, ClientException {
        Recommender recommender =new Recommender();
        recommender.setUid(uid);
        if(recommenderMapper.selectCount(recommender)<pageSize) {
            System.out.println("there");
            PageHelper.startPage(pageIndex, pageSize);
            List<Map<String, Object>> maps =pictureMapper.selectRandomMasterOutline("","","");
            urlUtil.cosURLPrefix(maps);
            return PageResult.wrap(maps);
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, Object>> maps =pictureMapper.getRecommend(uid);
        urlUtil.cosURLPrefix(maps);
        return PageResult.wrap(pictureMapper.getRecommend(uid));
    }


}
