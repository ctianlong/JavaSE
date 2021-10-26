package redis.cas;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import redis.RedisScriptHelper;

import java.util.concurrent.CountDownLatch;

/**
 * @author ctl
 * @date 2021/9/22
 */
public class RedisCasTest {

	public static String sb =
			"if redis.call('get', KEYS[1]) == ARGV[1] then\n" +
			"    redis.call('set', KEYS[1], ARGV[2])\n" +
			"    return 1\n" +
			"else\n" +
			"    return 0\n" +
			"end";
	public static RedisScript<Integer> script = RedisScriptHelper.createScript(sb, Integer.class);

	public static String sb1 =
			"if redis.call('hget', KEYS[1], ARGV[1]) == ARGV[2] then\n" +
			"    redis.call('hset', KEYS[1], ARGV[1], ARGV[3])\n" +
			"    return 1\n" +
			"else\n" +
			"    return 0\n" +
			"end";
	public static RedisScript<Integer> script1 = RedisScriptHelper.createScript(sb, Integer.class);

	public static String sb2 =
			"local keys,values=KEYS,ARGV\n" +
			"local old = redis.call('get',keys[1]) \n" +
			"if  values[1] == '' and old == false\n" +
			"then\n" +
			"\tredis.call('SET',keys[1],values[2])\n" +
			"\treturn 1\n" +
			"end\n" +
			"\n" +
			"if old == values[1]\n" +
			"then\n" +
			"\tredis.call('SET',keys[1],values[2])\n" +
			"\treturn 1\n" +
			"else\n" +
			"\treturn 0\n" +
			"end";
	public static RedisScript<Integer> script2 = RedisScriptHelper.createScript(sb, Integer.class);


	public static StringRedisTemplate getTemplate() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("");
		factory.setPort(6379);
		factory.setPassword("");
		factory.afterPropertiesSet();
		return new StringRedisTemplate(factory);
	}

	public static int num = 1000;
	public static CountDownLatch latch = new CountDownLatch(1);

	public static String cacheKey = "casKeyCtl";

	public static void main(String[] args) {

		RedisCasTest.Task task = new RedisCasTest.Task(getTemplate());
		for (int i = 0; i < num; i++) {
			new Thread(task, "test-" + i).start();
		}
		latch.countDown();
	}

	public static class Task implements Runnable {

		private StringRedisTemplate redisTemplate;

		private boolean quit = false;

		public Task(StringRedisTemplate redisTemplate) {
			this.redisTemplate = redisTemplate;
		}

		@Override
		public void run() {
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis());
			while (!quit) {
				long currentTime = 100000;
				Integer result = redisTemplate.execute(script, Lists.newArrayList(cacheKey), String.valueOf(currentTime));
				System.out.println(Thread.currentThread().getName() + ": [" + result + "]");
			}
		}
	}

}
