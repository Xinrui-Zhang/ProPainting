package seu.se.practice2.propainting.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TokenUtil {

	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static String buildToken(
		String rawTokenValue
	) {
		return Jwts.builder().setSubject(rawTokenValue).signWith(key).compact();
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TimedToken {

		private String value;
		private long startedTime;  // 创建时间戳
		private long ttl;  // time to live
		private long endedTime;  // 终止时间戳

		public static int ID = 0;

		private int id = TimedToken.ID++;

		public TimedToken(String tokenValue, long ttlMilliSeconds) {
			this.value = tokenValue;
			this.startedTime = new Date().getTime();
			this.ttl = ttlMilliSeconds;
			this.endedTime = this.startedTime + this.ttl;
			this.id = TimedToken.ID++;
		}

		public boolean isAlive() {
			return new Date(endedTime).after(new Date());
		}

		public String toString() {
			return value + ":" + startedTime + ":" + endedTime;
		}

		public int hashCode() {
			return id;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			TimedToken that = (TimedToken) o;
			return that.toString().equals(toString()) && that.id == id;
		}

		public boolean isSameTokenValue(String token) {
			return value.equals(token);
		}

	}
}
