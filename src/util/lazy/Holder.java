package util.lazy;

import java.util.function.Supplier;

/**
 * @author ctl
 * @date 2021/5/12
 */
public class Holder {

	// 默认第一次调用heavy.get()时触发的同步方法
	private Supplier<Heavy> heavy = () -> createAndCacheHeavy();
	public Holder() {
		System.out.println("Holder created");
	}
	public Heavy getHeavy() {
		// 第一次调用后heavy已经指向了新的instance，所以后续不再执行synchronized
		return heavy.get();
	}
	//...

	private synchronized Heavy createAndCacheHeavy() {
		// 方法内定义class，注意和类内的嵌套class在加载时的区别
		class HeavyFactory implements Supplier<Heavy> {
			// 饥渴初始化
			private final Heavy heavyInstance = new Heavy();
			public Heavy get() {
				// 每次返回固定的值
				return heavyInstance;
			}
		}

		//第一次调用方法来会将heavy重定向到新的Supplier实例
		if(!HeavyFactory.class.isInstance(heavy)) {
			heavy = new HeavyFactory();
		}
		return heavy.get();
	}

}
