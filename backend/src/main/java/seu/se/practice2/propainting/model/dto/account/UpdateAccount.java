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
public class UpdateAccount implements AllValidFields, ToAccount {

	protected String name = null;
	protected String avatar = null;
	protected String phone = null;


	@Override
	public boolean isAllValid() {
		return ValidationUtil.isNotNull(name) && ValidationUtil.isNotNull(avatar) &&
			ValidationUtil.isNotNull(phone);
	}

	@Override
	public Account toDefaultAccount() {
		Account account = new Account();
		BeanUtils.copyProperties(this, account);
		return account;
	}
}
