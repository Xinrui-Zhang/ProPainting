package seu.se.practice2.propainting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seu.se.practice2.propainting.aspect.WebResponseController;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.service.TestService;

@WebResponseController
@RestController
@CrossOrigin
public class TestController {

	@Autowired
	TestService testService;

	@GetMapping("/ping")
	public Object ping(
		@RequestParam Integer pageIndex,
		@RequestParam Integer pageSize
	) throws ClientException {
		return testService.ping(
			pageIndex, pageSize
		);
	}
}
