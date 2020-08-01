package seu.se.practice2.propainting.config.interceptor;

import com.google.gson.Gson;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import seu.se.practice2.propainting.component.token.LoginTokenManager;
import seu.se.practice2.propainting.controller.SearchController;
import seu.se.practice2.propainting.model.dto.ResultData;

@Component
public class TokenInterceptorAdapter extends HandlerInterceptorAdapter {

	public static final String TOKEN_NAME_IN_HEADER = "x-api-token";

	@Autowired
	private LoginTokenManager loginTokenManager;

	@SneakyThrows
	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {
		// 首先判断是否方法或者类上有没有验证 token 注解
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		TokenRequired tokenRequired = null;
		tokenRequired = handlerMethod.getMethod().getAnnotation(TokenRequired.class);
		if (tokenRequired == null) {
			tokenRequired = handlerMethod.getBeanType().getAnnotation(TokenRequired.class);
		}

		// 判断怎么处理
		if (tokenRequired == null) {
			return true;
		} else {
			boolean hasLoggedIn = true;

			// 判断是否登录
			String token;
			if ((token = request.getHeader(TOKEN_NAME_IN_HEADER)) == null ||
				token.equals("")
			) {
				hasLoggedIn = false;
			} else {
				hasLoggedIn = loginTokenManager.hasLoggedIn(token);
			}
			// 判断方法是否是SearchController里的方法
			Boolean isSearchController = handlerMethod.getBeanType().getName()
			.equals(SearchController.class.getName());
			// 直接拦截并且配置返回结果
			if (hasLoggedIn) {
				request.setAttribute("uid", loginTokenManager.getUID(token));
				return true;
			} else if(isSearchController){//未登录但属于SearchController里的方法，也放行
				return true;
			}
			else{
				response.reset();

				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain;charset=UTF-8");

				PrintWriter pw = response.getWriter();

				pw.write(
					new Gson().toJson(
						new ResultData().failure(
							ResultData.ResultDataCode.LOGIN_ERROR,
							"请先登录!"
						)
					)
				);

				pw.flush();
				pw.close();
				return false;
			}
		}
	}
}
