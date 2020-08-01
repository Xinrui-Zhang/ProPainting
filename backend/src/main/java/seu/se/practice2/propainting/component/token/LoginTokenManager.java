package seu.se.practice2.propainting.component.token;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.model.dao.entity.Account;
import seu.se.practice2.propainting.service.AccountService;
import seu.se.practice2.propainting.util.TokenUtil.TimedToken;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenManager {

	@Autowired
	private AccountService accountService;

	private ConcurrentHashMap<String, List<TimedToken>> emailToTokens =
		new ConcurrentHashMap<>();

	private ConcurrentHashMap<String, String> tokenToEmail =
		new ConcurrentHashMap<>();

	public static final long LOGIN_TOKEN_TTL = 120 * 60 * 1000;  // 120 min

	public void setToken(String email, TimedToken timedToken) {
		if (!emailToTokens.containsKey(email)) {
			emailToTokens.put(email, new LinkedList<>());
		}
		emailToTokens.get(email).add(timedToken);
		tokenToEmail.put(timedToken.getValue(), email);
	}

	public boolean hasLoggedIn(String token) {
		boolean ans = false;
		// 首先查看有没有这个token
		if (!tokenToEmail.containsKey(token)) {
			// 什么也不做
		} else {
			// 先获取到 email
			String email = tokenToEmail.get(token);
			// 再获取到 token list
			if (!emailToTokens.containsKey(email)) {
				tokenToEmail.remove(token);
			} else {
				List<TimedToken> timedTokens = emailToTokens.get(email);
				if (timedTokens == null || timedTokens.size() <= 0) {
					tokenToEmail.remove(token);
					emailToTokens.remove(email);
				} else {
					// 遍历每一个, 并且判断时间与删除
					int p = 0;
					while (p < timedTokens.size()) {
						TimedToken timedToken = timedTokens.get(p);
						if (!timedToken.isAlive()) {
							// token 不在存活
							timedTokens.remove(p);
						} else if (!timedToken.isSameTokenValue(token)) {
							// token 存活, 但是值不一样
							++p;
						} else {
							// token 存活, 且值一样, 即找到了 token
							ans = true;
							++p;
						}
					}
					if (!ans && timedTokens.size() == 0) {
						tokenToEmail.remove(token);
						emailToTokens.remove(email);
					}
				}
			}
		}
		return ans;
	}

	public Long getUID(String token) throws ServerException {
		String email = tokenToEmail.get(token);
		Account accountByEmail = accountService.getAccountByEmail(email);
		return accountByEmail.getUid();
	}

}
