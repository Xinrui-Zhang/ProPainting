package seu.se.practice2.propainting.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import seu.se.practice2.propainting.aspect.WebResponseMethod;
import seu.se.practice2.propainting.component.cos.COSFileManager;
import seu.se.practice2.propainting.config.interceptor.TokenRequired;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dao.entity.Fileinfo;
import seu.se.practice2.propainting.model.dao.mapper.FileinfoMapper;
import seu.se.practice2.propainting.model.dto.TransferPic;
import seu.se.practice2.propainting.service.PictureService;
import seu.se.practice2.propainting.util.URLUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.List;

/**
 * author: leimei date: 2020/7/18 9:29
 */
@CrossOrigin
@RestController
public class CommonController {

	 @Autowired
	 COSFileManager cosFileManager;

	@Autowired
	private FileinfoMapper fileinfoMapper;
	@Autowired
	private PictureService pictureService;
	
	 @TokenRequired
	 @PostMapping("/resource")
	 public Object postResource(
	 	@RequestBody MultipartFile multipartFile
	 ) throws IOException {
	 	return cosFileManager.doUploadMultipartFile(multipartFile);
	 }

	// 下载图片
	@GetMapping(path = "/fileinfo")
	@WebResponseMethod
	public Object getFileinfo(@RequestParam Integer pid) throws ServerException, ClientException {
		Fileinfo f = new Fileinfo();
		f.setPid(pid);
		List<Fileinfo> select = fileinfoMapper.select(f);
		select.forEach(s -> {
			if (s.getUrl().startsWith("picture")) {
				s.setUrl(URLUtil.cosPrefix + s.getUrl());
			}
		});
		return select;
	}

	// 生成色卡
	@GetMapping(path = "/card")
	@WebResponseMethod
	public Object getColorCard(@RequestParam Integer pid) throws ServerException, ClientException, ServiceException {
		return pictureService.getColorCard(pid);
	}

	// 图像风格迁移
	@PostMapping(path = "/transfer")
	@WebResponseMethod
	public Object transferPic(@ModelAttribute TransferPic transferPic) throws ServerException, ClientException, ServiceException {
		return pictureService.transferPic(transferPic);
	}
	// qa
	@GetMapping("/qa")
	@WebResponseMethod
	public Object getQA(
		@RequestParam String question
	) throws ServiceException, IOException {
		// 处理问题
		BufferedWriter bufferedWriter = null;
		BufferedReader bufferedReader = null;
		Socket socket = null;
		SocketAddress socketAddress = new InetSocketAddress("172.18.0.3", 18085);
		long startTimestamp = new Date().getTime();
		// 重试十秒
		while (true) {
			try {
				socket = new Socket();
				socket.connect(socketAddress);
				break;
			} catch (IOException ignored) {
				// 尝试超过 10s
				if (new Date().getTime() - startTimestamp > 10 * 1000) {
					throw new ServiceException("智能客服目前正在维护!");
				}
			}
		}
		try {
			question = question.replace("$", "");
			question = question + "$";
			bufferedWriter =
				new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedWriter.write(question);
			bufferedWriter.flush();
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return bufferedReader.readLine();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException ignored) {

				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ignored) {

				}
			}
			try {
				socket.close();
			} catch (IOException ignored) {

			}
		}
	}
}
