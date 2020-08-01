package seu.se.practice2.propainting.component.token;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationTokenManager extends AbstractSimpleEmailTokenManager {

	public static final long REGISTRATION_TOKEN_TTL = 5 * 60 * 1000;  // 5 min

	protected long timeToLive = REGISTRATION_TOKEN_TTL;  // 5 min
}
