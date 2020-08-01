package seu.se.practice2.propainting.service;

import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.model.dao.entity.Account;
import seu.se.practice2.propainting.model.dto.account.LoginAccount;
import seu.se.practice2.propainting.model.dto.account.RegistrationAccount;
import seu.se.practice2.propainting.model.dto.account.UpdateAccount;
import seu.se.practice2.propainting.validation.NotNull;

@Validated
public interface AccountService {

	void postAccount(
		@NotNull RegistrationAccount registrationAccount
	) throws ClientException, ServerException;

	String getAccountLogin(@NotNull LoginAccount loginAccount) throws ClientException, ServerException;

	Account getAccountByEmail(@NotNull String email) throws ServerException;

	void putAccountInformation(@NotNull Long uid, @NotNull UpdateAccount updateAccount);

	void putAccountPassword(@NotNull RegistrationAccount retrieveAccount)
		throws ClientException, ServerException;

	Account getAccountInformation(@NotNull Long uid);
}
