package seu.se.practice2.propainting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seu.se.practice2.propainting.aspect.WebResponseMethod;
import seu.se.practice2.propainting.config.interceptor.TokenRequired;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dto.PicInfoAndFile;
import seu.se.practice2.propainting.model.dto.userTool.*;
import seu.se.practice2.propainting.model.dto.ResultData;
import seu.se.practice2.propainting.service.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class UserToolController {

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private RequestService requestService;

	@Autowired
	private SketchrecordService sketchrecordService;

	// todo: 以下接口是 meilei 改写的
	// swagger 是接口管理工具, 可以通过注解的方式生成 api 接口文档
	// 我们项目使用了 NEI 作为接口管理平台, 因此, 不需要使用 swagger
	// 如果 swagger 还有其他功能可以后续跟我交流, 再修改代码
//    @ApiOperation("新增图片到某收藏夹")
	// 通过这个注解实现对结果的统一封装
	// 如果抛出异常, 那么就会被捕获, 包装成 400 的错误响应返回
    // 前端收到:
    // code=400(或500, 根据异常类型自动判断),
    // msg={请求失败的原因} xxx,
    // data=null
	// 如果正常响应, 那么也会被捕获, 包装成 200 的 ok 响应
    // 前端收到:
    // code=200,
    // msg=ok,
    // data=你返回的对象的 json 格式
	// 具体逻辑可以看一下 WebResponseAspect::doWrapForResultData 这个方法
	@WebResponseMethod
	// 这个注解会拦截这个接口的请求, 先对用户的身份进行判断
	// 前端会把 token 写在 header 中, 这个注解会从 header 中获取到 token
	// 然后再和我们的缓存进行比对, 如果一致, 那么才会进入这个请求, 不然就会被拦截
	@TokenRequired
	@PostMapping(path = "/collection")
	// 注意这里的返回值, 如果使用了 @WebResponseMethod 这个注解, 那么统一使用 Object
	// 当然你使用 ResultData 也是可以的, 只不过动态性差一些, 建议使用 Object
	public Object addPictoCollect(
			// 在不需要使用 token, 没有必要, 因为 token 校验的逻辑是一样的
			// 每个接口都写一遍代码质量差, 因此通过 @TokenRequired 接口统一拦截
//            @RequestHeader("token") String token,
			@RequestBody collectMap collectMap
			) throws ServiceException, ClientException {
	    // 如果要通过下面这个方式获取到用户的 uid, 做 join 操作
        // 那么一定要在方法上 标注 @TokenRequired 注解
        // uid 是在方法拦截的时候, 被解析计算之后才插入到 request 中去的, 前端不会传过来
        Long uid = (Long) request.getAttribute("uid");
	    // service 也该写了, 追踪进去看一下
		Integer pid=collectMap.getPid();
		Integer cid=collectMap.getCid();
		String type=collectMap.getType();
		collectionService.insertPictoCollect(pid, cid, type,uid);
		// 这里返回了一个 String
        // 最终前端会收到 code: 200, msg: 'ok', data: 收藏成功!
        // 如果你使用下面这个代码
        // return new ResultData.SuccessMessage("收藏成功!");
        // 那么, 前端会收到 code: 200, msg: '收藏成功!', data: null
        // 这也就是为什么返回类型设定为 Object, 可以实现自动包装
		return "收藏成功!";
	}

	@WebResponseMethod
	@TokenRequired
	@DeleteMapping(path = "/collection")
	public Object deletePictoCollect(
		@RequestParam Integer pid,
		@RequestParam Integer cid,
		@RequestParam String type
	) throws ServiceException, ClientException {

		collectionService.deletePictoCollect(pid, cid, type);
		return "删除成功";
	}


	@WebResponseMethod
	@TokenRequired
	@PostMapping(path = "/create")
	public Object addCollection(
			@RequestBody createCollect
createCollect			) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		String title=createCollect.getTitle();
		String intro=createCollect.getIntro();
		String visibility=createCollect.getVisibility();
		collectionService.createCollection(
			title, intro, visibility, uid);
		return "新增成功";
	}

	// 底下4个接口由shuxinyue改动
	// 管理员新增图片
	@TokenRequired
	@WebResponseMethod
    @PostMapping(path="/picture")
    public Object addPicture(
		 @ModelAttribute PicInfoAndFile picInfoAndFile
    ) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		// Long uid = Long.valueOf("1");
		pictureService.addPicture(picInfoAndFile,"1", uid);
		return  "添加成功！";
    }

	// 管理员删除图片
	@TokenRequired
	@WebResponseMethod
	@DeleteMapping(path = "/picture")
	public Object deletePicture(
		@RequestParam Integer pid
	) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		// Long uid= Long.valueOf(1);
		pictureService.deletePicture(pid, uid);
		return  "删除成功！";
	}


	// 管理员修改图片
	@TokenRequired
	@WebResponseMethod
	@PutMapping(path = "/picture")
	public Object updatePicture(
		@ModelAttribute PicInfoAndFile picInfoAndFile
	) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		// Long uid= Long.valueOf(1);
		pictureService.updatePicture(picInfoAndFile,uid);
		return  "修改成功！";
	}

	// 上传作品
	@TokenRequired
	@WebResponseMethod
	@PostMapping(path = "/picture/user")
	public Object uploadPicture(
		@ModelAttribute PicInfoAndFile picInfoAndFile
	) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		// Long uid = Long.valueOf("1");
		pictureService.addPicture(picInfoAndFile,"2", uid);
		return  "上传成功！";
	}


	@WebResponseMethod
	@TokenRequired
	@PostMapping(path = "/admire")
	public Object likePicture(
			@RequestBody admireTool
admireTool			) throws ServiceException, ClientException {
		//       Long uid=Long.valueOf(2);
		Long uid = (Long) request.getAttribute("uid");
		Integer pid=admireTool.getPid();
		Integer like=admireTool.getLike();
		String type=admireTool.getType();
		pictureService.updateLike(like, pid, type, uid);
		if(like==0)
			return "点赞成功";
		else
			return "取消成功";
	}


	@WebResponseMethod
	@TokenRequired
	@PostMapping(path = "/visibility/picture")
	public Object updateVisibilityP(
			@RequestBody visibilityTool visibilityTool			)
			throws ServiceException, ClientException {
		Integer pid=visibilityTool.getPid();
		String visibility=visibilityTool.getVisibility();
		pictureService.updateVisibiltyP(pid, visibility);
		if(visibility.equals("1"))
			return "设置为可见";
		else
			return "设置为不可见";
	}

	@WebResponseMethod
	@TokenRequired
	@PostMapping(path = "/visibility/set")
	public Object updateVisibilityS(
			@RequestBody visibilitySet visibilityTool
	) throws ServiceException, ClientException {
		Integer cid=visibilityTool.getCid();
		String visibility=visibilityTool.getVisibility();
		collectionService.updateVisibiltyS(cid, visibility);
		if(visibility.equals("1"))
			return "设置为可见";
		else
			return "设置为不可见";
	}

	@WebResponseMethod
	@TokenRequired
	@GetMapping(path = "/message/detail")
	public Object checkMsg(
		@RequestParam Integer mid
	) throws ServiceException, ClientException {
		return messageService.getMessage(mid);
	}

	@WebResponseMethod
	@TokenRequired
	@GetMapping(path = "/message/list")
	public Object userMessage() throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		return messageService.userMessage(uid);
	}



	@WebResponseMethod
	@TokenRequired
	@PostMapping(path = "/request")
	public Object postRequest(
		@RequestBody Requests requestBody
	) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		String type = requestBody.getType();
		Integer pid = requestBody.getPid();
		String content = requestBody.getContent();
		requestService.postRequest(type, pid, content, uid);
		return "提交成功";
	}


	@WebResponseMethod
	@TokenRequired
	@PostMapping(path = "/dealRequest")
	public Object dealRequest(
		@RequestBody dealTool dealTool
	) throws ServiceException, ClientException {
		Integer rid=dealTool.getRid();
		String content=dealTool.getContent();
		Long uid = (Long) request.getAttribute("uid");
		requestService.dealRequest(rid, content, uid);
		return "处理成功";
	}


	@WebResponseMethod
	@TokenRequired
	@GetMapping(path = "/request")
	public Object checkRequest(
		@RequestParam Integer pageIndex,
		@RequestParam Integer pageSize
	) throws ServiceException, ClientException {
		return requestService.checkRequest(pageIndex, pageSize);
	}


	@WebResponseMethod
	@TokenRequired
	@GetMapping(path = "/collection")
	public Object getCollection(
			@RequestParam Integer pageIndex,
			@RequestParam Integer pageSize
	) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		return collectionService.getCollection(uid,pageIndex,pageSize);
	}



	@WebResponseMethod
	@GetMapping(path = "/collection/picture")
	public Object getPictureList(
		@RequestParam Integer cid,
		@RequestParam Integer pageIndex,
		@RequestParam Integer pageSize
	) throws ServiceException, ClientException {
		return collectionService.getPictureList(cid,pageIndex,pageSize);
	}

	@WebResponseMethod
	@TokenRequired
	@GetMapping(path = "/collection/collection")
	public Object getCollectionList(
		@RequestParam Integer pageIndex,
		@RequestParam Integer pageSize
	) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		return collectionService.getCollectionList(uid,pageIndex,pageSize);
	}




	@WebResponseMethod
	@TokenRequired
	@GetMapping(path = "/sketchrecord")
	public Object checkRecord(
		@RequestParam Integer pageIndex,
		@RequestParam Integer pageSize
	) throws ServiceException, ClientException {
		Long uid = (Long) request.getAttribute("uid");
		return sketchrecordService.checkRecord(pageIndex, pageSize, uid);
	}


	@WebResponseMethod
	@TokenRequired
	@PostMapping(path = "/sketchrecord")
	public ResultData addRecord(
		@RequestBody Integer[] pids
	) throws ServiceException, ClientException {
//        Long uid=Long.valueOf(1);
		Long uid = (Long) request.getAttribute("uid");
		return sketchrecordService.addRecord(pids, uid);
	}

}
