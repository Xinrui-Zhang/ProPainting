package seu.se.practice2.propainting.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.model.dao.mapper.CollectionMapper;
import seu.se.practice2.propainting.model.dao.mapper.PictureMapper;
import seu.se.practice2.propainting.model.dto.PageResult;
import seu.se.practice2.propainting.model.dto.SketchfabResponse;
import seu.se.practice2.propainting.service.SearchService;
import seu.se.practice2.propainting.util.URLUtil;

@Service
public class SearchServiceImpl implements SearchService{
    @Autowired
    PictureMapper pictureMapper;
    @Autowired
    CollectionMapper collectionMapper;
    @Autowired
    URLUtil urlUtil;

    private final String SKETCHFAB_SEARCH_URL = "https://api.sketchfab.com/v3/search?type=models&q={0}&cursor={1}&count={2}&date={3}&min_face_count={4}&max_face_count={5}&pbr_type={6}&animated={7}&sort_by={8}";

    @Override
    public PageResult getMasterOutline(
        String q, String genre, String tech,String style,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize
    ) throws ServerException, ClientException{
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, Object>> maps =
            pictureMapper.selectMasterOutline(q, genre, tech, style);
        urlUtil.cosURLPrefix(maps);
        return PageResult.wrap(maps);
        // 拦截器将已登录用户的搜索结果用个性算法重排、过滤后返回
    }

    @Override
    public PageResult getRandomMasterOutline(
        String genre, String tech,String style,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize
    ) throws ServerException, ClientException{
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, Object>> maps =pictureMapper.selectRandomMasterOutline(genre,tech,style);
        urlUtil.cosURLPrefix(maps);
        return PageResult.wrap(maps);
    }
    
    @Override
    public PageResult getReferOutline(
        String q,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize
    ) throws ServerException, ClientException {
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, Object>> maps = pictureMapper.selectReferOutline(q);
        urlUtil.cosURLPrefix(maps);
        return PageResult.wrap(maps);
        // 拦截器将已登录用户的搜索结果用个性算法重排、过滤后返回
    }

    @Override
    public Object getThreeDModel(
        String q,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize,
        Integer period,
        Integer minFaces,
        Integer maxFaces,
        Integer pbr,
        Boolean animated,
        Integer sort
    ) throws ServerException, ClientException{
        RestTemplate restTemplate = new RestTemplate();
        String res =restTemplate.getForObject(SKETCHFAB_SEARCH_URL,String.class,q,
        Integer.toString((pageIndex-1)*pageSize),pageSize.toString(),
        period,minFaces,maxFaces,pbr,animated,sort);

        List<SketchfabResponse> result = new ArrayList<SketchfabResponse>();

        JSONObject jsonObject = JSONObject.parseObject(res);
        JSONArray results = jsonObject.getJSONArray("results");
        for (Object  obj: results){
            JSONObject jobj = (JSONObject) obj;
            String name = jobj.getString("name");
            JSONObject thumbnailsObj = jobj.getJSONObject("thumbnails");
            JSONArray thumbnails = thumbnailsObj.getJSONArray("images");
            String thumbnail = "";
            Integer mid =(thumbnails.size()+1)/2;
            Integer i=0;
            for(Object thumbnailObj:thumbnails){
                i++;
                if(i>=mid){
                    thumbnail = ((JSONObject)thumbnailObj).getString("url");
                    break;
                }
            }
            String embedUrl = jobj.getString("embedUrl");
            Integer likeCount = jobj.getInteger("likeCount");
            Integer viewCount = jobj.getInteger("viewCount");
            Integer faceCount = jobj.getInteger("faceCount");

            result.add(new SketchfabResponse(name,thumbnail,embedUrl,viewCount,likeCount,faceCount));
        }
        return result;
    }

    @Override
    public PageResult getCollectionOutline(
        String q,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize
    ) throws ServerException, ClientException {
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, Object>> maps = collectionMapper.selectCollectionOutline(q);
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
    public Object getPictureDetail(Integer pid, Long uid
    ) throws ServerException, ClientException{
        List<Map<String, Object>> maps = pictureMapper.selectPictureDetail(pid, uid);
        urlUtil.cosURLPrefix(maps);
        return maps;
    }

    @Override
    public Object getCollectionDetail(Integer cid, Long uid
    ) throws ServerException, ClientException{
        List<Map<String, Object>> maps = collectionMapper.selectCollectionDetail(cid, uid);
        urlUtil.cosURLPrefix(maps);
        return maps;
    }
}