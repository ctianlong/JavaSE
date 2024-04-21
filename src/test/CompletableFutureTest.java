package test;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ctl
 * @createTime 2023/05/31 10:30
 * @description
 */
public class CompletableFutureTest {

    @Test
    public void test1() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<CompletableFuture> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread().getName() + " start");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
                System.out.println(Thread.currentThread().getName() + " end");
            }, executorService));
        }

        CompletableFuture<Void> endFuture = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
        endFuture.join();

        System.out.println("---end---");

    }

}
