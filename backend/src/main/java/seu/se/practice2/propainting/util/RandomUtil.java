package seu.se.practice2.propainting.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import seu.se.practice2.propainting.error.ClientException;

public class RandomUtil {

	public static final List<Character> NUMBERS =
		new ArrayList<Character>(10) {
			{
				for (int i = 0; i < 10; ++i) {
					add((char) ('0' + i));
				}
			}
		};

	public static final List<Character> LOWER_LETTERS =
		new ArrayList<Character>() {
			{
				for (int i = 0; i < 26; ++i) {
					add((char) ('a' + i));
				}
			}
		};

	public static final List<Character> UPPER_LETTERS =
		new ArrayList<Character>() {
			{
				for (int i = 0; i < 26; ++i) {
					add((char) ('A' + i));
				}
			}
		};

	public static final Random random = new Random();

	public static String randomlySelectNumbers(int len) throws ClientException {
		if (len <= 0) {
			throw new ClientException("随机码长度必须大于0!");
		}
		StringBuilder ans = new StringBuilder();
		for (int i = 0; i < len; ++i) {
			ans.append(NUMBERS.get(random.nextInt(NUMBERS.size())));
		}
		return ans.toString();
	}

}
