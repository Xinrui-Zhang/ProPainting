package seu.se.practice2.propainting.model.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import seu.se.practice2.propainting.model.dao.entity.Account;
import seu.se.practice2.propainting.util.ValidationUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginAccount implements AllValidFields, ToAccount {

	protected String email = null;
	protected String password = null;

	@Override
	public boolean isAllValid() {
		return ValidationUtil.isNotNull(email) && ValidationUtil.isNotNull(password);
	}

	@Override
	public Account toDefaultAccount() {
		Account account = new Account();
		BeanUtils.copyProperties(this, account);
		return account;
	}
}
