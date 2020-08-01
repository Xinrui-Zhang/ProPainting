package seu.se.practice2.propainting.component.token;

import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import seu.se.practice2.propainting.util.TokenUtil.TimedToken;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractSimpleEmailTokenManager {

	protected ConcurrentHashMap<String, TimedToken> emailTokenMap = new ConcurrentHashMap<>();

	protected long timeToLive = 5 * 60 * 1000;  // 5 min

	public void setEmailToken(String email, String verifyCode) {
		emailTokenMap.put(
			email, new TimedToken(verifyCode, this.timeToLive)
		);
	}

	public TimedToken tryAcquireToken(String email) {
		return emailTokenMap.get(email);
	}

	public void removeToken(String email) {
		emailTokenMap.remove(email);
	}

}
