package juc.nx.aqs;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

/**
 * 使用CAS实现一把简单的锁
 * @author ctl
 * @date 2021/7/17
 */
public class CustomLock {

	private volatile int state = 0;

	private static Unsafe unsafe;
	private static long stateOffset;

	static {
		Field unsafeFiled;
		try {
			unsafeFiled = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeFiled.setAccessible(true);
			unsafe = (Unsafe) unsafeFiled.get(null);
			stateOffset = unsafe.objectFieldOffset(CustomLock.class.getDeclaredField("state"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void lock() {
		while (!compareAndSet(0, 1)) {
			// 没获取到锁，自旋，会尝试再次去获取
		}
	}

	public void unlock() {
		state = 0;
	}

	private boolean compareAndSet(int oldVal, int newVal) {
		return unsafe.compareAndSwapInt(this, stateOffset, oldVal, newVal);
	}

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		CustomLock lock = new CustomLock();
		new Thread(() -> {
			try {
				System.out.println("t1 try lock");
				lock.lock();
				System.out.println("t1 lock succ");
				// 确保t1先获取到锁，countDown在此处调用才是真正百分百确保t1的逻辑会被先执行，本质是要求t1先获取到锁
				latch.countDown();
				Thread.sleep(2000L);
				System.out.println("t1-run");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("t1 try unlock");
				lock.unlock();
				System.out.println("t1 unlock succ");
			}
		}, "t1").start();

		latch.await();

		new Thread(() -> {
			try {
				System.out.println("t2 try lock");
				lock.lock();
				System.out.println("t2 lock succ");
				System.out.println("t2-run");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("t2 try unlock");
				lock.unlock();
				System.out.println("t2 unlock succ");
			}
		}, "t2").start();
	}

}
