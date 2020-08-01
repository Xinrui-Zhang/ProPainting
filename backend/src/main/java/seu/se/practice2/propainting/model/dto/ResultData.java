package seu.se.practice2.propainting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ResultData {

	private Integer code;
	private String msg;
	private Object data;

	public ResultData() {
		setAll(ResultDataCode.DEFAULT_SUCCESS, SuccessMessage.DEFAULT_SUCCESS_MESSAGE, null);
	}

	public ResultData setAll(Integer s, String m, Object d) {
		code = s;
		msg = m;
		data = d;
		return this;
	}

	public ResultData success(String m, Object d) {
		return setAll(ResultDataCode.DEFAULT_SUCCESS, m, d);
	}

	public ResultData success(Object d) {
		return setAll(ResultDataCode.DEFAULT_SUCCESS, SuccessMessage.DEFAULT_SUCCESS_MESSAGE, d);
	}

	public ResultData failure(Integer s, String m, Object d) {
		return setAll(s, m, d);
	}

	public ResultData failure(Integer s, String m) {
		return setAll(s, m, "");
	}

	public ResultData failure(String m) {
		return setAll(ResultDataCode.DEFAULT_ERROR, m, null);
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SuccessMessage {

		public static final String DEFAULT_SUCCESS_MESSAGE = "ok";
		private String successMessage = DEFAULT_SUCCESS_MESSAGE;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class FailureMessage {

		public static final String DEFAULT_FAILURE_MESSAGE = "请求失败!";
		private String failureMessage = DEFAULT_FAILURE_MESSAGE;
	}

	public static class ResultDataCode {

		public static final Integer DEFAULT_SUCCESS = 200;
		public static final Integer DEFAULT_ERROR = 400;
		public static final Integer LOGIN_ERROR = 401;
		public static final Integer AUTH_ERROR = 403;
		public static final Integer NOT_FOUND_ERROR = 404;
		public static final Integer SERVER_ERROR = 500;
	}

	public static ResultData wrap(Object object) {
		if (object instanceof ResultData) {
			return (ResultData) object;
		} else if (object instanceof FailureMessage) {
			return new ResultData().failure(((FailureMessage) object).failureMessage);
		} else if (object instanceof SuccessMessage) {
			return new ResultData().success(((SuccessMessage) object).successMessage, null);
		} else {
			// 如果返回一个 Object 默认为成功的 data
			return new ResultData().success(object);
		}
	}
}
