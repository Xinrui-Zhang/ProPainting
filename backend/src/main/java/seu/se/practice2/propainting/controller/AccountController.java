package seu.se.practice2.propainting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seu.se.practice2.propainting.aspect.WebResponseController;
import seu.se.practice2.propainting.config.interceptor.TokenRequired;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.model.dto.account.*;
import seu.se.practice2.propainting.service.AccountService;
import seu.se.practice2.propainting.service.EmailService;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@RestController
@WebResponseController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private HttpServletRequest request;


	@PostMapping("/verifyCode")
	public Object verifyCodeAdaptor(
		@RequestBody VerifyCode verifyCode
	) throws ClientException, ExecutionException, InterruptedException, ServerException {
		String email = verifyCode.getEmail();
		Integer purpose = verifyCode.getPurpose();
		if (purpose == 1 || purpose == 2) {
			return emailService.sendVerifyCode(email, purpose);
		} else {
			throw new ServerException("验证码作用 purpose 不属于约定范围!");
		}
	}

	@PostMapping
	public Object postAccount(
		@RequestBody RegistrationAccountWithMore registrationAccount
	) throws ClientException, ServerException {
		accountService.postAccount(registrationAccount);
		return "注册成功!";
	}

	@PostMapping("/login")
	public Object getAccountLogin(
		@RequestBody LoginAccount loginAccount
	) throws ServerException, ClientException {
		return accountService.getAccountLogin(loginAccount);
	}

	@TokenRequired
	@PutMapping("/information")
	public Object putAccountInformation(
		@RequestBody UpdateAccount updateAccount
	) {
		Long uid = (Long) request.getAttribute("uid");
		accountService.putAccountInformation(uid, updateAccount);
		return "修改账户信息成功!";
	}

	@PutMapping("/password")
	public Object putAccountPassword(
		@RequestBody RegistrationAccount retrieveAccount
	) throws ServerException, ClientException {
		accountService.putAccountPassword(retrieveAccount);
		return "密码找回成功!";
	}

	@TokenRequired
	@GetMapping("/information")
	public Object getAccountInformation() {
		Long uid = (Long) request.getAttribute("uid");
		return accountService.getAccountInformation(uid);
	}

}
