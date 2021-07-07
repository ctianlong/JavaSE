package util.lazy;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 惰性求值，延迟加载，在真正使用到的时候才去进行计算；
 * 该类线程安全；
 * @author ctl
 * @date 2020/11/24
 */
public class LazySupplier<T> implements Supplier<T> {

	private Supplier<T> supplier;
	private volatile boolean initialized;
	private volatile T value;

	public LazySupplier(Supplier<T> supplier) {
		this.supplier = Objects.requireNonNull(supplier, "Lazy Supplier can not be null");
	}

	@Override
	public T get() {
		// 双重检验锁实现单例
		if (!initialized) {
			synchronized (this) {
				if (!initialized) {
					value = supplier.get();
					initialized = true;
					// help GC
					supplier = null;
				}
			}
		}
		return value;
	}

	/**
	 * 映射操作，返回结果仍为延迟初始化对象
	 */
	public <R> LazySupplier<R> map(Function<? super T, ? extends R> mapper) {
		return LazySupplier.of(() -> mapper.apply(this.get()));
	}

	/**
	 * 扁平化映射操作，返回结果仍为延迟初始化对象
	 */
	public <R> LazySupplier<R> flatMap(Function<? super T, LazySupplier<R>> mapper) {
		return LazySupplier.of(() -> mapper.apply(this.get()).get());
	}

	/**
	 * 过滤操作，返回结果仍为延迟初始化对象
	 */
	public LazySupplier<Optional<T>> filter(Predicate<? super T> predicate) {
		return LazySupplier.of(() -> Optional.ofNullable(this.get()).filter(predicate));
	}

	public void consume(Consumer<? super T> consumer) {
		consumer.accept(this.get());
	}

	public boolean isInitialized() {
		return initialized;
	}

	public static <T> LazySupplier<T> of(Supplier<T> supplier) {
		return new LazySupplier<>(supplier);
	}

}
