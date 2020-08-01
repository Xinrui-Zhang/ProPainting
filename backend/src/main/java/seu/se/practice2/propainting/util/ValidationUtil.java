package seu.se.practice2.propainting.util;

public class ValidationUtil {

	public static boolean isNotNull(Object o) {
		return (o != null && !o.equals(""));
	}

}
