package juc;

import java.util.concurrent.CyclicBarrier;

/*
 * 模拟CAS算法
 */
public class TestCompareAndSwap {
	
	public static void main(String[] args) {
		int n = 10;
		CyclicBarrier cyclicBarrier = new CyclicBarrier(n);
		final CompareAndSwap cas = new CompareAndSwap();
		for (int i = 0; i < n; i++) {
			new Thread(() -> {
				try {
					// 尽量让10个线程同时执行
					cyclicBarrier.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
				boolean b = cas.compareAndSet(0, 1);
				// 最终只有一个打印true
				System.out.println(Thread.currentThread().getName() + ": " + b);
			}, "t-" + i).start();
		}
	}

}

class CompareAndSwap {
	
	private volatile int value;
	
	//获取内存值
	public int get() {
		return value;
	}

	//设置
	public boolean compareAndSet(int expectedValue, int newValue) {
		return expectedValue == compareAndSwap(expectedValue, newValue);
	}
	
	//比较，软件模拟使用synchronized保证原子性，实际CAS操作是硬件会保证原子性
	private synchronized int compareAndSwap(int expectedValue, int newValue) {
		int oldValue = value;
		
		if (oldValue == expectedValue) {
			this.value = newValue;
		}
		
		return oldValue;
	}

}
