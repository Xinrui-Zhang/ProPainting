package seu.se.practice2.propainting.service.impl;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.component.token.RegistrationTokenManager;
import seu.se.practice2.propainting.component.token.RetrieveTokenManager;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.service.EmailService;
import seu.se.practice2.propainting.util.RandomUtil;
import seu.se.practice2.propainting.validation.NotNull;

@Validated
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	ThreadPoolTaskExecutor executor;

	@Autowired
	RegistrationTokenManager registrationTokenManager;

	@Autowired
	RetrieveTokenManager retrieveTokenManager;

	public static final String EMAIL_SENDER = "propainting@163.com";
	public static final String EMAIL_SENDER_PASSWORD = "RPNJCFQNSKDXKOIP";
	public static final String EMAIL_SENDER_HOST = "smtp.163.com";

	@Override
	public boolean isEmail(String email) {
		Matcher matcher = EMAIL_PATTERN.matcher(email);
		return matcher.matches();
	}

	@Override
	public void sendEmail(
		@NotNull String subject,
		@NotNull String content,
		@NotNull String sendTo
	) throws MessagingException {
		String from = EMAIL_SENDER;
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", EMAIL_SENDER_HOST);
		properties.setProperty("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(
			properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
						from, EMAIL_SENDER_PASSWORD
					);
				}
			});
		// 创建邮件对象
		Message message = new MimeMessage(session);
		// 设置发件人
		message.setFrom(new InternetAddress(from));
		// 设置接收人
		message.addRecipient(
			Message.RecipientType.TO, new InternetAddress(sendTo)
		);
		// 设置邮件主题
		message.setSubject(subject);
		// 设置邮件内容
		message.setContent(content, "text/html;charset=UTF-8");
		// 发送邮件
		Transport.send(message);
	}

	@Override
	public String buildRegistrationVerifyCodeContentForEmail(
		@NotNull String email
	) throws ClientException {
		// 用户登录生成一个 6 位验证码
		String verifyCode = RandomUtil.randomlySelectNumbers(REG_RANDOM_LEN);
		registrationTokenManager.setEmailToken(email, verifyCode);
		return String.format(
			"您的验证码为: %s, 有效时间为 %d 分钟. 请在规定时间内使用, 切勿泄露给他人.",
			verifyCode, (RegistrationTokenManager.REGISTRATION_TOKEN_TTL / 1000 / 60)
		);
	}

	@Override
	public String sendVerifyCode(
		@NotNull String email,
		@NotNull Integer purpose
	) throws ExecutionException, InterruptedException {
		Future<String> future = executor.submit(
			new Callable<String>() {
				@SneakyThrows
				@Override
				public String call() throws Exception {
					if (!isEmail(email)) {
						throw new ClientException(
							String.format(
								"邮箱 %s 不合法!",
								email
							)
						);
					}
					String content = "", subject = "", reply = "";
					switch (purpose) {
						case 1:
							content = buildRegistrationVerifyCodeContentForEmail(email);
							subject = "欢迎注册使用ProPainting";
							reply = "注册验证码已发送!";
							break;
						case 2:
							content = buildRetrieveVerifyCodeContentForEmail(email);
							subject = "ProPainting密码找回";
							reply = "找回密码验证码已发送!";
							break;
						case 3:
							throw new ServerException("验证码作用 purpose 不属于约定范围!");
					}
					sendEmail(subject, content, email);
					return reply;
				}
			}
		);
		return future.get();
	}

	@Override
	public String buildRetrieveVerifyCodeContentForEmail(
		@NotNull String email
	) throws ClientException {
		String verifyCode = RandomUtil.randomlySelectNumbers(REG_RANDOM_LEN);
		retrieveTokenManager.setEmailToken(email, verifyCode);
		return String.format(
			"您的验证码为: %s, 有效时间为 %d 分钟. 请在规定时间内使用, 切勿泄露给他人.",
			verifyCode, (RetrieveTokenManager.RETRIEVE_TOKEN_TTL / 1000 / 60)
		);
	}
}
