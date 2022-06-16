package test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author ctl
 * @date 2021/12/1
 */
public class GuavaTest {

	@Test
	public void guava() throws InterruptedException {
		Cache<String, String> cache = CacheBuilder.newBuilder()
				.expireAfterWrite(5, TimeUnit.SECONDS)
				.build();
		System.out.println(cache.getIfPresent("key"));
		cache.put("key", "vvv");
		System.out.println(cache.getIfPresent("key"));
		Thread.sleep(4000);
		System.out.println(cache.getIfPresent("key"));
		Thread.sleep(2000);
		System.out.println(cache.getIfPresent("key"));

	}
}
