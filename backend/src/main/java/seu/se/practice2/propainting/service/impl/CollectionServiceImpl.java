package seu.se.practice2.propainting.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dao.entity.Account;
import seu.se.practice2.propainting.model.dao.entity.Collect;
import seu.se.practice2.propainting.model.dao.entity.Collectmap;
import seu.se.practice2.propainting.model.dao.entity.Likemap;
import seu.se.practice2.propainting.model.dao.entity.Picture;
import seu.se.practice2.propainting.model.dao.mapper.AccountMapper;
import seu.se.practice2.propainting.model.dao.mapper.CollectionMapper;
import seu.se.practice2.propainting.model.dao.mapper.CollectmapMapper;
import seu.se.practice2.propainting.model.dao.mapper.LikemapMapper;
import seu.se.practice2.propainting.model.dao.mapper.PictureMapper;
import seu.se.practice2.propainting.model.dto.PageResult;
import seu.se.practice2.propainting.model.dto.ResultData;
import seu.se.practice2.propainting.service.CollectionService;
import seu.se.practice2.propainting.validation.NotNull;
import seu.se.practice2.propainting.model.dto.PageResult;
import org.hibernate.validator.constraints.Range;
import com.github.pagehelper.PageHelper;
import seu.se.practice2.propainting.util.URLUtil;

@Validated
@Service
public class CollectionServiceImpl implements CollectionService {

	@Autowired
	private CollectionMapper collectionMapper;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private CollectmapMapper collectmapMapper;
	@Autowired
	private LikemapMapper likemapMapper;
	@Autowired
	private PictureMapper pictureMapper;
	@Autowired
    URLUtil urlUtil;

	@Override
	public void insertPictoCollect(
	    @NotNull Integer pid,
        @NotNull Integer cid,
        @NotNull String type,
		@NotNull Long uid
    ) throws ServiceException {
	    // 这里需要对业务逻辑进行判断, 比较简单的方式是直接使用 mapper 进行插入
        // 当然, 如果直接插入, 那么就会依赖 SQL 去完成逻辑判断, 比如说 pid = -1
        // 这样的作法不保险, 应该先查 select 判定有效性, 然后再插入
        // 这里采用简单的插入就可以了, 具体的逻辑你们可以具体设计
        collectmapMapper.insertPictoCollect(pid, cid, type);
//		return ResultData.wrap(collectmapMapper.insertPictoCollect(pid, cid, type));
	}

	@Override
	public void deletePictoCollect(@NotNull Integer pid, @NotNull Integer cid,
		@NotNull String type) throws ServiceException {
		Collectmap collectmap = new Collectmap();
		collectmap.setCid(cid);
		collectmap.setId(pid);
		collectmap.setType(type);
		collectmapMapper.delete(collectmap);
	}

	@Override
	public void createCollection(@NotNull String title,@NotNull String intro,@NotNull String visibility,
		@NotNull Long uid) throws ServiceException {
		Account account = new Account();
		account.setUid(uid);
		String avatar = accountMapper.selectByPrimaryKey(account).getAvatar();
		Collect collect = new Collect();
		collect.setAvatar(avatar);
		collect.setTitle(title);
		collect.setIntro(intro);
		collect.setVisibility(visibility);
		collect.setUid(Math.toIntExact(uid));
		collect.setState("1");
		collect.setCtime(new Date());
		collect.setEtime(new Date());
		 ResultData.wrap(collectionMapper.insertSelective(collect));
	}

	@Override
	public void updateVisibiltyS(@NotNull Integer cid,@NotNull String visibility)
		throws ServiceException, ClientException {
		collectionMapper.updateVisibiltyS(cid, visibility);
	}

	@Override
	public Object getCollection(@NotNull Long uid,@Range(min = 0) Integer pageIndex,
								@Range(min = 0) Integer pageSize) throws ServiceException, ClientException {
		List<Map<String, Object>> list = new LinkedList<>();

		Collect collect = new Collect();
		collect.setUid(Math.toIntExact(uid));
		PageHelper.startPage(pageIndex, pageSize);
		Iterator<Collect> iterator = collectionMapper.select(collect).iterator();
		while (iterator.hasNext()) {
			Map<String, Object> map = new HashMap<>();
			Collect collect1 = iterator.next();
			Integer cid = collect1.getCid();
			map.put("collection", collect1);
			Collectmap collectmap = new Collectmap();
			collectmap.setCid(cid);
			Iterator<Collectmap> iteratorMap = collectmapMapper.select(collectmap).iterator();
			int i = 0;
			List<String> urls = new LinkedList<>();
			List<Integer> widths = new LinkedList<>();
			List<Integer> heights = new LinkedList<>();
			while (i < 4 && iteratorMap.hasNext()) {
				Collectmap curMap = iteratorMap.next();
				Integer pid = curMap.getId();   //拿到collectMap中某一ID
				String type = curMap.getType();
                if (type.equals("1"))   //是图片
                {
                    i++;
                    String url = pictureMapper.selectByPrimaryKey(pid).getUrl();
                    Integer width = pictureMapper.selectByPrimaryKey(pid).getWidth();
                    Integer height = pictureMapper.selectByPrimaryKey(pid).getHeight();
                    urls.add(url);
                    widths.add(width);
                    heights.add(height);
                } else {
                    continue;
                }
			}
			map.put("coverUrl", urls);
			map.put("coverWidth", widths);
			map.put("coverHeight", heights);
			list.add(map);

		}
		return list;
	}

	@Override
	public PageResult getPictureList(
		@NotNull Integer cid,
		@Range(min = 0) Integer pageIndex,
		@Range(min = 0) Integer pageSize)
		throws ServiceException, ClientException {
			PageHelper.startPage(pageIndex, pageSize);
			List<Map<String, Object>> maps =collectionMapper.selectPictureList(cid);
        	urlUtil.cosURLPrefix(maps);
			return PageResult.wrap(maps);
	}

	
	@Override
	public PageResult getCollectionList(
		@NotNull Long uid,
		@Range(min = 0) Integer pageIndex,
		@Range(min = 0) Integer pageSize)
		throws ServiceException, ClientException{
			PageHelper.startPage(pageIndex, pageSize);
			List<Map<String, Object>> maps =collectionMapper.selectCollectionList(uid);
			maps.forEach(map -> {
				String urls = (String) map.get("urls");
				String[] _urls = urls.split("\\|");
				for (int i = 0; i < _urls.length; ++i) {
					_urls[i] = (_urls[i].startsWith("picture") ? URLUtil.cosPrefix : "") + _urls[i];
				}
				StringBuilder sb = new StringBuilder();
				for (String _u: _urls) {
					sb.append("|").append(_u);
				}
				map.put("urls", sb.substring(1));
			});
			return PageResult.wrap(maps);
		}

	@Override
	public Map getOthersCollection(@NotNull Integer cid,@NotNull Long uid)
		throws ServiceException, ClientException {
		Map<String, Object> map = new HashMap<String, Object>();
		Collect collect = new Collect();
		collect.setCid(cid);
		map.put("collection", collectionMapper.select(collect));
		Integer collected = 1;
		Integer liked = 1;
		Likemap likemap = new Likemap();
		likemap.setUid(Math.toIntExact(uid));
		likemap.setId(cid);
		likemap.setType("2");
        if (likemapMapper.selectCount(likemap) > 0) {
            liked = 0;  //点过赞
        } else {
            liked = 1;  //未点过
        }
		map.put("liked", liked);

		//是否收藏过
		Collectmap collectmap = new Collectmap();
		collectmap.setId(cid);
		collectmap.setType("2");
		System.out.println(collectmapMapper.select(collectmap));
		Iterator<Collectmap> iterator = collectmapMapper.select(collectmap).iterator();
		while (iterator.hasNext()) {
			Collectmap curMap = iterator.next();
			System.out.println(curMap);
			Integer curCid = curMap.getCid();
			Collect collect1 = new Collect();
			collect1.setCid(curCid);
			Integer curUid = collectionMapper.selectByPrimaryKey(collect1).getUid();
			if (uid == Long.valueOf(curUid))     //说明uid这个用户有收藏夹收藏过这个收藏
			{
				collected = 0;
				System.out.println("收藏过");
				break;
			}
		}
		map.put("collected", collected);
		return map;
	}



}
