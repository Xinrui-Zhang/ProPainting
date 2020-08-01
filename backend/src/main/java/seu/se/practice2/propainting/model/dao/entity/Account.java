package seu.se.practice2.propainting.model.dao.entity;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

	@Id
	private Long uid = null;

	private String email = null;

	private String name = null;

	private String password = null;

	private String avatar = null;

	private String phone = null;

	private Integer contribution = null;

	private Integer points = null;

	private Integer authority = null;
}