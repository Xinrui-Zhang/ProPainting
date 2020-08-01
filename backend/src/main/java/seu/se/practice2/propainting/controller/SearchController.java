package seu.se.practice2.propainting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seu.se.practice2.propainting.aspect.WebResponseMethod;
import seu.se.practice2.propainting.config.interceptor.TokenRequired;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.service.RecommenderService;
import seu.se.practice2.propainting.service.SearchService;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private RecommenderService recommenderService;

    @Autowired
	private HttpServletRequest request;

    //搜索大师作品
    @TokenRequired
    @WebResponseMethod
    @GetMapping(path = "/picture/master/outline")
    public Object getMasterOutline(
        @RequestParam String q, 
        @RequestParam Integer pageIndex,
        @RequestParam Integer pageSize,
        @RequestParam(required = false) String genre, 
        @RequestParam(required = false) String tech,
        @RequestParam(required = false) String style
    )throws ServerException, ClientException,ServiceException {

        Long uid = (Long) request.getAttribute("uid");
         if(q.equals("")&&uid!=null){
             return recommenderService.getRecommend(uid,pageIndex,pageSize); //智能推荐
         }
         else {
            //游客随机推荐
            if(q.equals("")){
                return searchService.getRandomMasterOutline(genre,tech,style,pageIndex,pageSize);
            }
            else{
                return searchService.getMasterOutline(q,genre,tech,style,pageIndex,pageSize);
            }
         }

    }
    
    // 搜索参考图片
    @TokenRequired
    @WebResponseMethod
    @GetMapping(path = "/picture/refer/outline")
    public Object getReferOutline(
        @RequestParam String q, 
		@RequestParam Integer pageIndex,
        @RequestParam Integer pageSize
    )throws ServerException, ClientException {
        return searchService.getReferOutline(q,pageIndex,pageSize);
    }

    // 搜索资源集
    @TokenRequired
    @WebResponseMethod
    @GetMapping(path = "/collection/outline")
    public Object getCollectionOutline(
        @RequestParam String q, 
		@RequestParam Integer pageIndex,
        @RequestParam Integer pageSize
    )throws ServerException, ClientException {
        return searchService.getCollectionOutline(q,pageIndex,pageSize);
    }

    // 搜索3d资源
    @WebResponseMethod
    @GetMapping(path = "/3dmodel")
    public Object get3dModelOutline(
        @RequestParam String q,
        @RequestParam Integer pageIndex,
		@RequestParam Integer pageSize,
        @RequestParam(required = false) Integer period,
        @RequestParam(required = false) Integer minFaces,
        @RequestParam(required = false) Integer maxFaces,
        @RequestParam(required = false) Integer pbr,
        @RequestParam(required = false) Boolean animated,
        @RequestParam(required = false) Integer sort
    )throws ServerException, ClientException {
            return searchService.getThreeDModel(q,pageIndex,pageSize,period,minFaces,maxFaces,pbr,animated,sort);
    }

    
    // 获取图片详情
    @TokenRequired
    @WebResponseMethod
    @GetMapping(path = "/picture/detail")
    public Object getPictureDetail(@RequestParam Integer pid
    ) throws ServerException, ClientException {
        Long uid = (Long) request.getAttribute("uid");
        // Long uid= Long.valueOf(1);
        return searchService.getPictureDetail(pid, uid);
    }

    // 获取资源集详情
    @TokenRequired
    @WebResponseMethod
    @GetMapping(path = "/collection/detail")
    public Object getCollectionDetail(@RequestParam Integer cid
    ) throws ServerException, ClientException {
        Long uid = (Long) request.getAttribute("uid");
        // Long uid= Long.valueOf(1);
        return searchService.getCollectionDetail(cid, uid);
    }
}