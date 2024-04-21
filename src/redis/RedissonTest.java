package redis;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import util.common.SleepUtils;

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

	@Test
	public void redisLock() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		RedissonClient redisson = Redisson.create(config);
		String lockKey = "mylock";
		String lockKey1 = "mylock1";
		RLock lock = redisson.getLock(lockKey);
		RLock lock1 = redisson.getLock(lockKey1);
		try {
			lock.lock();
			System.out.println("get lock");
//			lock1.lock();
			// 业务模拟
			System.out.println("start do something");
			SleepUtils.sleepSeconds(30);
			System.out.println("end do something");
//			lock.lock();
//			System.out.println("get lock1");
//			System.out.println("start do something1");
//			SleepUtils.sleepSeconds(30);
//			System.out.println("end do something1");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
			System.out.println("unlock");
//			lock1.unlock();
//			lock.unlock();
//			System.out.println("unlock1");
		}



	}


	/**
	 * 两个线程同时获取一把锁流程（手动设置过期时间使看门狗不启动），redis命令执行监控：
	 *
	 * 1657095178.116699 [0 172.17.0.1:37622] "EVAL" "if (redis.call('exists', KEYS[1]) == 0) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; return redis.call('pttl', KEYS[1]);" "1" "mylock" "40000" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:45"
	 * 1657095178.116784 [0 lua] "exists" "mylock"
	 * 1657095178.116795 [0 lua] "hincrby" "mylock" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:45" "1"
	 * 1657095178.116810 [0 lua] "pexpire" "mylock" "40000"
	 *
	 * 1657095178.116838 [0 172.17.0.1:37666] "EVAL" "if (redis.call('exists', KEYS[1]) == 0) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; return redis.call('pttl', KEYS[1]);" "1" "mylock" "40000" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46"
	 * 1657095178.116894 [0 lua] "exists" "mylock"
	 * 1657095178.116901 [0 lua] "hexists" "mylock" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46"
	 * 1657095178.116912 [0 lua] "pttl" "mylock"
	 *
	 * 1657095178.165273 [0 172.17.0.1:37674] "SUBSCRIBE" "redisson_lock__channel:{mylock}"
	 *
	 * 1657095178.180202 [0 172.17.0.1:37642] "EVAL" "if (redis.call('exists', KEYS[1]) == 0) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; return redis.call('pttl', KEYS[1]);" "1" "mylock" "40000" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46"
	 * 1657095178.180334 [0 lua] "exists" "mylock"
	 * 1657095178.180347 [0 lua] "hexists" "mylock" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46"
	 * 1657095178.180365 [0 lua] "pttl" "mylock"
	 *
	 * 1657095208.154175 [0 172.17.0.1:37602] "EVAL" "if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then return nil;end; local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); if (counter > 0) then redis.call('pexpire', KEYS[1], ARGV[2]); return 0; else redis.call('del', KEYS[1]); redis.call('publish', KEYS[2], ARGV[1]); return 1; end; return nil;" "2" "mylock" "redisson_lock__channel:{mylock}" "0" "40000" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:45"
	 * 1657095208.154656 [0 lua] "hexists" "mylock" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:45"
	 * 1657095208.154726 [0 lua] "hincrby" "mylock" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:45" "-1"
	 * 1657095208.154789 [0 lua] "del" "mylock"
	 * 1657095208.154816 [0 lua] "publish" "redisson_lock__channel:{mylock}" "0"
	 *
	 * 1657095208.171868 [0 172.17.0.1:37630] "EVAL" "if (redis.call('exists', KEYS[1]) == 0) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; return redis.call('pttl', KEYS[1]);" "1" "mylock" "40000" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46"
	 * 1657095208.172093 [0 lua] "exists" "mylock"
	 * 1657095208.172117 [0 lua] "hincrby" "mylock" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46" "1"
	 * 1657095208.172151 [0 lua] "pexpire" "mylock" "40000"
	 *
	 * 1657095208.180112 [0 172.17.0.1:37674] "UNSUBSCRIBE" "redisson_lock__channel:{mylock}"
	 *
	 * 1657095238.203581 [0 172.17.0.1:37618] "EVAL" "if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then return nil;end; local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); if (counter > 0) then redis.call('pexpire', KEYS[1], ARGV[2]); return 0; else redis.call('del', KEYS[1]); redis.call('publish', KEYS[2], ARGV[1]); return 1; end; return nil;" "2" "mylock" "redisson_lock__channel:{mylock}" "0" "40000" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46"
	 * 1657095238.203739 [0 lua] "hexists" "mylock" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46"
	 * 1657095238.203768 [0 lua] "hincrby" "mylock" "1da1da8f-27d9-4ae1-a8f2-959b9e2ce590:46" "-1"
	 * 1657095238.203793 [0 lua] "del" "mylock"
	 * 1657095238.203805 [0 lua] "publish" "redisson_lock__channel:{mylock}" "0"
	 */
	@Test
	public void redisLockMultiThread() {
		// 两个线程同时获取一把锁流程（手动设置过期时间使看门狗不启动）
		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		RedissonClient redisson = Redisson.create(config);
		String lockKey = "mylock";
		RLock lock = redisson.getLock(lockKey);

		new Thread(() -> {
			try {
				lock.lock(40, TimeUnit.SECONDS);
				System.out.println("Thread1 get lock");
				// 业务模拟
				System.out.println("Thread1 start do something");
				SleepUtils.sleepSeconds(30);
				System.out.println("Thread1 end do something");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
				System.out.println("Thread1 unlock");
			}
		}).start();

		new Thread(() -> {
			try {
				lock.lock(40, TimeUnit.SECONDS);
				System.out.println("Thread2 get lock");
				// 业务模拟
				System.out.println("Thread2 start do something");
				SleepUtils.sleepSeconds(30);
				System.out.println("Thread2 end do something");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
				System.out.println("Thread2 unlock");
			}
		}).start();

		SleepUtils.sleepSeconds(100);

	}

	public static void main(String[] args) {
		System.out.println("if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then " +
				"return nil;" +
				"end; " +
				"local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); " +
				"if (counter > 0) then " +
				"redis.call('pexpire', KEYS[1], ARGV[2]); " +
				"return 0; " +
				"else " +
				"redis.call('del', KEYS[1]); " +
				"redis.call('publish', KEYS[2], ARGV[1]); " +
				"return 1; " +
				"end; " +
				"return nil;");
	}



}
