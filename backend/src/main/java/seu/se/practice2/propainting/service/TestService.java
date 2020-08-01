package seu.se.practice2.propainting.service;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.model.dto.PageResult;

@Validated
public interface TestService {

	PageResult ping(
		@Range(min = 0) Integer pageIndex,
		@Range(min = 0) Integer pageSize
	) throws ClientException;
}
