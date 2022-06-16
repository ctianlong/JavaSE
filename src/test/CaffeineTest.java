package test;

import com.github.benmanes.caffeine.cache.*;
import com.google.common.testing.FakeTicker;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/8/21
 */
public class CaffeineTest {

    @Test
    public void testAsync() {
        AsyncCacheLoader<String, Object> cacheLoader = new AsyncCacheLoader<String, Object>() {
            @Override
            public @NonNull CompletableFuture<Object> asyncLoad(@NonNull String s, @NonNull Executor executor) {
                return CompletableFuture.supplyAsync(() -> {
                    System.out.println("load: 1");
                    return 1;
                }, executor);
            }

            @Override
            public @NonNull CompletableFuture<Object> asyncReload(@NonNull String key, @NonNull Object oldValue, @NonNull Executor executor) {
                return CompletableFuture.supplyAsync(() -> {
                    int i = (int) oldValue + 1;
                    System.out.println("reload: " + i);
                    return i;
                }, executor);
            }
        };
        String key = "name1";
        // 用户测试，一个时间源，返回一个时间值，表示从某个固定但任意时间点开始经过的纳秒数。
        FakeTicker ticker = new FakeTicker();
        AsyncLoadingCache<String, Object> cache = Caffeine.newBuilder()
                .removalListener((String k, Object graph, RemovalCause cause) ->
                        System.out.printf("执行移除监听器- Key %s Value %s was removed (%s)%n", k, graph, cause))
                .ticker(ticker::read)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                // 指定在创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
                .refreshAfterWrite(4, TimeUnit.SECONDS)
                .buildAsync(cacheLoader);
    }

    @Test
    public void testRefresh() {

        CacheLoader<String, Object> cacheLoader = new CacheLoader<String, Object>() {
            @Override
            public Object load(@NonNull String s) throws Exception {
                System.out.println("load: 1");
                return 1;
            }

            @Override
            public Object reload(@NonNull String key, @NonNull Object oldValue) {
                int i = (int) oldValue + 1;
                System.out.println("reload: " + i);
                return i;
            }
        };

        String key = "name1";
        // 用户测试，一个时间源，返回一个时间值，表示从某个固定但任意时间点开始经过的纳秒数。
        FakeTicker ticker = new FakeTicker();

        // 基于固定的到期策略进行退出
        // expireAfterAccess
        LoadingCache<String, Object> cache = Caffeine.newBuilder()
                .removalListener((String k, Object graph, RemovalCause cause) ->
                        System.out.printf("执行移除监听器- Key %s Value %s was removed (%s)%n", k, graph, cause))
                .ticker(ticker::read)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                // 指定在创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
                .refreshAfterWrite(4, TimeUnit.SECONDS)
                .build(cacheLoader);

        System.out.println("第一次获取缓存");
        System.out.println(cache.get(key));

        System.out.println("等待4.5S后，第二次次获取缓存");
        // 直接指定时钟
        ticker.advance(4500, TimeUnit.MILLISECONDS);
        System.out.println(cache.get(key));

        System.out.println("等待3.8S后，第三次次获取缓存");
        // 直接指定时钟
        ticker.advance(3800, TimeUnit.MILLISECONDS);
        System.out.println(cache.get(key));

    }




}
