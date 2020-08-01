package seu.se.practice2.propainting.config.web;


import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import seu.se.practice2.propainting.model.dto.ResultData;
import seu.se.practice2.propainting.model.dto.ResultData.ResultDataCode;

@ControllerAdvice
@Slf4j
public class DefaultExceptionResolverConfigurer {

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResultData handleException(Throwable e) {
		// 打印异常堆栈信息
		log.error(e.getMessage(), e);
		return new ResultData(
			ResultDataCode.DEFAULT_ERROR,
			e.getMessage(),
			Arrays.toString(e.getStackTrace())
		);
	}
}
