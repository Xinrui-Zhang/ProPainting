package seu.se.practice2.propainting.component.id;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IDGenerator {

	/**
	 * 毫秒内序列(0~4095)
	 */
	private long sequence = 0L;

	/**
	 * 上次生成ID的时间截
	 */
	private long lastTimestamp = -1L;


	/**
	 * 获得下一个ID (该方法是线程安全的)
	 *
	 * @return SnowflakeId
	 */
	public synchronized long nextId() {
	    // 当前系统时间戳
		long currentTimestamp = currentSystemTimestamp();

		// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if (currentTimestamp < lastTimestamp) {
			throw new RuntimeException(
				String.format(
					"Clock moved backwards. Refusing to generate id for %d milliseconds",
					lastTimestamp - currentTimestamp
				)
			);
		}

        /*
          序列在id中占的位数
         */
		long sequenceBits = 10L;
		if (lastTimestamp == currentTimestamp) {
			// 如果是同一时间生成的, 则进行毫秒内序列
            /*
              生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
             */
			long sequenceMask = ~(-1L << sequenceBits);
			sequence = (sequence + 1) & sequenceMask;
			// 毫秒内序列溢出
			if (sequence == 0) {
				// 阻塞到下一个毫秒,获得新的时间戳
				currentTimestamp = blockTillNextMillis(lastTimestamp);
			}
		}
		else {
			// 时间戳改变, 毫秒内序列重置
			sequence = 0L;
		}

		// 上次生成ID的时间截
		lastTimestamp = currentTimestamp;

		// 移位并通过或运算拼到一起组成64位的ID
        /*
          开始时间截 (2019-08-01)
         */
		long epoch = 1564588800000L;
        /*
          时间截向左移(10)
         */
		long temp = ((currentTimestamp - epoch) << sequenceBits) | sequence;
		return (temp << 12) >> 12 | sequence;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 *
	 * @return 当前时间(毫秒)
	 */
	protected long currentSystemTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 *
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long blockTillNextMillis(long lastTimestamp) {
		long timestamp = currentSystemTimestamp();
		while (timestamp <= lastTimestamp) {
			timestamp = currentSystemTimestamp();
		}
		return timestamp;
	}
}
