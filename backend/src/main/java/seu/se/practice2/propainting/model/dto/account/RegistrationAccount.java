package seu.se.practice2.propainting.model.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import seu.se.practice2.propainting.util.ValidationUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAccount extends LoginAccount implements AllValidFields, ToAccount {

	protected String verifyCode = null;

	@Override
	public boolean isAllValid() {
		return ValidationUtil.isNotNull(email) && ValidationUtil.isNotNull(password)
			&& ValidationUtil.isNotNull(verifyCode);
	}

	public boolean isValidPassword() {
		return password.length() >= 6;
	}
}
