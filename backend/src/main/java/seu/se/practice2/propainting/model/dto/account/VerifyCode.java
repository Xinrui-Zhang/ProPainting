package seu.se.practice2.propainting.model.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: leimei date: 2020/7/22 2:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCode {

	String email = "";

	Integer purpose = 1;
}
