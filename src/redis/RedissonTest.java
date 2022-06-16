package redis;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author ctl
 * @date 2021/12/20
 */
public class RedissonTest {

	@Test
	public void delayedQueue() throws InterruptedException {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		RedissonClient redisson = Redisson.create(config);
		RBlockingQueue<Object> blockingQueue = redisson.getBlockingQueue("dest-queue-1");
		RDelayedQueue<Object> delayedQueue = redisson.getDelayedQueue(blockingQueue);

		new Thread(() -> {
			while (true) {
				try {
					Object msg = blockingQueue.take();
					System.out.println(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		delayedQueue.offer("msg-1", 10L, TimeUnit.SECONDS);

//		for (int i = 1; i <= 5; i++) {
//			delayedQueue.offer("msg-" + i, i * 10L, TimeUnit.SECONDS);
//		}

		Thread.sleep(20000L);

	}
}
