package seu.se.practice2.propainting.service.impl;

import com.github.pagehelper.PageHelper;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.model.dao.mapper.TestMapper;
import seu.se.practice2.propainting.model.dto.PageResult;
import seu.se.practice2.propainting.service.TestService;

@Validated
@Service
public class TestServiceImpl implements TestService {

	@Autowired
	TestMapper testMapper;

	@Override
	public PageResult ping(
		@Range(min = 0) Integer pageIndex,
		@Range(min = 0) Integer pageSize
	) throws ClientException {
		PageHelper.startPage(pageIndex, pageSize);
		return PageResult.wrap(testMapper.selectAll());
	}
}
