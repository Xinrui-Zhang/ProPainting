package seu.se.practice2.propainting.component.token;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveTokenManager extends AbstractSimpleEmailTokenManager {

	public static final long RETRIEVE_TOKEN_TTL = 5 * 60 * 1000;  // 5 min

	protected long timeToLive = RETRIEVE_TOKEN_TTL;  // 5 min
}
