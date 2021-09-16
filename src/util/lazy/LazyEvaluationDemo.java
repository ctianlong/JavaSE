package util.lazy;

import java.util.function.Supplier;

/**
 * @author ctl
 * @date 2021/8/31
 */
public class LazyEvaluationDemo {

	public static Heavy expensiveCompute() {
		// long time computation...
		System.out.println("compute heavy");
		return new Heavy();
	}

	public static void business1(Heavy target, boolean conditionA) {
		// ...
		if (conditionA) {
			System.out.println("logicA use heavy: " + target);
		}
		// ...
	}

	public static void main(String[] args) {
		// 立即求值，可能造成对象被计算但未使用，资源浪费
		Heavy target = expensiveCompute();
		business1(target, false);

		// 惰性求值，对象最终未使用则不计算
		Supplier<Heavy> targetSupplier = () -> expensiveCompute();
		business1WithLazy(targetSupplier, false);

		// 惰性求值，但求值计算进行了2遍，实际只应该执行1遍即可
		Supplier<Heavy> targetSupplier2 = () -> expensiveCompute();
		business2WithLazy(targetSupplier2, true, true);
	}

	public static void business1WithLazy(Supplier<Heavy> targetSupplier, boolean conditionA) {
		// ...
		if (conditionA) {
			System.out.println("logicA use heavy: " + targetSupplier.get());
		}
		// ...
	}

	public static void business2WithLazy(Supplier<Heavy> targetSupplier, boolean conditionA, boolean conditionB) {
		// ...
		if (conditionA) {
			System.out.println("logicA use heavy: " + targetSupplier.get());
		}
		if (conditionB) {
			System.out.println("logicB use heavy: " + targetSupplier.get());
		}
		// ...
	}

}
