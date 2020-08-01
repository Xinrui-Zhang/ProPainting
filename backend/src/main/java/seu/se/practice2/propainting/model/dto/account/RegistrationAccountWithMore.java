package seu.se.practice2.propainting.model.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: leimei date: 2020/7/18 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAccountWithMore extends RegistrationAccount {

	protected String name = null;
	protected String phone = null;



}
