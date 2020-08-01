package seu.se.practice2.propainting.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.model.dto.ResultData;
import seu.se.practice2.propainting.model.dto.ResultData.ResultDataCode;

@Aspect
@Component
public class WebResponseAspect {

	@Pointcut("@annotation(seu.se.practice2.propainting.aspect.WebResponseMethod)")
	public void methodAPI() {
	}

	@Pointcut("@within(seu.se.practice2.propainting.aspect.WebResponseController)")
	public void typeAPI() {
	}

	@Around("methodAPI()")
	public Object wrapResultForMethod(ProceedingJoinPoint proceedingJoinPoint) {
		return doWrapForResultData(proceedingJoinPoint);
	}

	private Object doWrapForResultData(ProceedingJoinPoint proceedingJoinPoint) {
		Object ans;
		try {
			ans = proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			if (throwable instanceof ServerException) {
				ans = new ResultData().failure(
					ResultDataCode.SERVER_ERROR,
					"服务器出错: " + throwable.getMessage(),
					null
				);
			} else {
				ans = new ResultData.FailureMessage(throwable.getMessage());
			}
		}
		return ResultData.wrap(ans);
	}

	@Around("typeAPI()")
	public Object wrapResultForController(ProceedingJoinPoint proceedingJoinPoint) {
		return doWrapForResultData(proceedingJoinPoint);
	}
}
