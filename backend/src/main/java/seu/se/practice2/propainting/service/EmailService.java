package seu.se.practice2.propainting.service;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.validation.NotNull;

@Validated
public interface EmailService {

	Pattern EMAIL_PATTERN = Pattern.compile(
		"^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

	int REG_RANDOM_LEN = 6;

	boolean isEmail(String email);

	void sendEmail(
		@NotNull String subject,
		@NotNull String content,
		@NotNull String sendTo
	) throws MessagingException;

	String buildRegistrationVerifyCodeContentForEmail(@NotNull String email) throws ClientException;

	String buildRetrieveVerifyCodeContentForEmail(@NotNull String email) throws ClientException;

	String sendVerifyCode(
		@NotNull String email,
		@NotNull Integer purpose
	) throws ClientException, ExecutionException, InterruptedException;
}
