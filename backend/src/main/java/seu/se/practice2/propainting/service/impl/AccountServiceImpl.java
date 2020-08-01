package seu.se.practice2.propainting.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.component.token.LoginTokenManager;
import seu.se.practice2.propainting.component.token.RegistrationTokenManager;
import seu.se.practice2.propainting.component.token.RetrieveTokenManager;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.model.dao.entity.Account;
import seu.se.practice2.propainting.model.dao.mapper.AccountMapper;
import seu.se.practice2.propainting.model.dto.account.LoginAccount;
import seu.se.practice2.propainting.model.dto.account.RegistrationAccount;
import seu.se.practice2.propainting.model.dto.account.UpdateAccount;
import seu.se.practice2.propainting.service.AccountService;
import seu.se.practice2.propainting.util.RandomUtil;
import seu.se.practice2.propainting.util.TokenUtil;
import seu.se.practice2.propainting.util.TokenUtil.TimedToken;
import seu.se.practice2.propainting.validation.NotNull;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Validated
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private RegistrationTokenManager registrationTokenManager;

	@Autowired
	private RetrieveTokenManager retrieveTokenManager;

	@Autowired
	private LoginTokenManager loginTokenManager;

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public void postAccount(
		@NotNull RegistrationAccount registrationAccount
	) throws ClientException, ServerException {
		if (!registrationAccount.isAllValid()) {
			throw new ClientException("注册信息不全!");
		}
		String email = registrationAccount.getEmail();
		TimedToken timedToken =
			registrationTokenManager.tryAcquireToken(email);
		if (timedToken == null) {
			throw new ClientException("请先获取验证码!");
		}
		if (!timedToken.isAlive()) {
			registrationTokenManager.removeToken(email);
			throw new ClientException("当前验证码已失效, 请重新获取!");
		}
		// 校验验证码
		String verifyCode = registrationAccount.getVerifyCode();
		if (!verifyCode.equals(timedToken.getValue())) {
			throw new ClientException("输入的验证码错误!");
		} else {
			Account account;
			account = getAccountByEmail(email);
			if (account != null) {
				// 删除注册验证码
				registrationTokenManager.removeToken(email);
				throw new ClientException(
					String.format(
						"当前邮箱 %s 已经注册!", email
					)
				);
			} else {
				// 判断密码长度
				if (!registrationAccount.isValidPassword()) {
					throw new ClientException("当前密码不符合要求, 长度至少为 6 位!");
				}
				// 验证码正确, 执行插入
				account = registrationAccount.toDefaultAccount();
				accountMapper.insertSelective(account);
				// 删除注册验证码
				registrationTokenManager.removeToken(email);
			}
		}
	}

	@Override
	public String getAccountLogin(
		@NotNull LoginAccount loginAccount
	) throws ClientException, ServerException {
		if (!loginAccount.isAllValid()) {
			throw new ClientException("用户邮箱或密码缺失!");
		}
		String email = loginAccount.getEmail();
		Account account = getAccountByEmail(email);
		if (!account.getPassword().equals(loginAccount.getPassword())) {
			throw new ClientException("密码错误, 请重试或找回密码!");
		}
		// 账号密码均正确, 生成token
		String rawTokenValue =
			email + ":" + RandomUtil.randomlySelectNumbers(8) + ":" + (new Date().getTime());
		String loginTokenValue = TokenUtil.buildToken(rawTokenValue);
		TimedToken timedToken = new TimedToken(loginTokenValue, LoginTokenManager.LOGIN_TOKEN_TTL);
		loginTokenManager.setToken(email, timedToken);
		// 返回token给c端
		return loginTokenValue;
	}

	@Override
	public Account getAccountByEmail(@NotNull String email) throws ServerException {
		List<Account> accounts =
			accountMapper.selectByExample(
				Example.builder(Account.class)
					.where(Sqls.custom().andEqualTo("email", email))
					.build()
			);
		if (accounts.size() > 1) {
			throw new ServerException("邮箱 " + email + " 存在多条账户记录!");
		} else if (accounts.size() == 1) {
			return accounts.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void putAccountInformation(
		@NotNull Long uid, @NotNull UpdateAccount updateAccount
	) {
		Account account = updateAccount.toDefaultAccount();
		account.setUid(uid);
		accountMapper.updateByPrimaryKeySelective(account);
	}

	@Override
	public void putAccountPassword(
		@NotNull RegistrationAccount retrieveAccount
	) throws ClientException, ServerException {
		if (!retrieveAccount.isAllValid()) {
			throw new ClientException("修改密码信息不全!");
		}
		@SuppressWarnings("")
		String email = retrieveAccount.getEmail();
		TimedToken timedToken = retrieveTokenManager.tryAcquireToken(email);
		if (timedToken == null) {
			throw new ClientException("请先获取验证码!");
		}
		if (!timedToken.isAlive()) {
			retrieveTokenManager.removeToken(email);
			throw new ClientException("当前验证码已失效, 请重新获取!");
		}
		if (!timedToken.isSameTokenValue(retrieveAccount.getVerifyCode())) {
			throw new ClientException("输入的验证码错误!");
		}
		Account account = getAccountByEmail(email);
		if (account == null) {
			throw new ClientException("当前账户还没有注册, 请先完成注册!");
		}
		if (account.getPassword().equals(retrieveAccount.getPassword())) {
			throw new ClientException("新密码最近被使用, 请设置新的密码!");
		} else if (!retrieveAccount.isValidPassword()) {
			throw new ClientException("当前密码不符合要求, 长度至少为 6 位!");
		} else {
			Account newAccount = new Account();
			newAccount.setUid(account.getUid());
			newAccount.setPassword(retrieveAccount.getPassword());
			accountMapper.updateByPrimaryKeySelective(newAccount);
		}
	}

	@Override
	public Account getAccountInformation(@NotNull Long uid) {
		Account account = accountMapper.selectByPrimaryKey(uid);
		account.setPassword("");
		return account;
	}
}
