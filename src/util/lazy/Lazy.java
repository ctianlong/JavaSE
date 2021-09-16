package util.lazy;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 惰性求值，真正使用时才去进行计算；
 * 后续可直接复用该结果，不会再次计算；
 * 该类线程安全；
 */
public class Lazy<T> implements Supplier<T> {
	private Supplier<T> delegate; // 对象实际提供者
	private volatile boolean initialized; // 是否已初始化
	private volatile T value; // 目标对象

	public Lazy(Supplier<T> delegate) {
		this.delegate = Objects.requireNonNull(delegate, "Lazy Supplier can not be null");
	}

	@Override
	public T get() {
		// 双重检验锁实现单例
		if (!initialized) {
			synchronized (this) {
				if (!initialized) {
					value = delegate.get();
					initialized = true;
					delegate = null; // help GC
				}
			}
		}
		return value;
	}

	/** 映射操作，返回结果仍为延迟初始化对象 */
	public <R> Lazy<R> map(Function<? super T, ? extends R> mapper) {
		return Lazy.of(() -> mapper.apply(this.get()));
	}

	/** 扁平化映射操作，返回结果仍为延迟初始化对象 */
	public <R> Lazy<R> flatMap(Function<? super T, Lazy<R>> mapper) {
		return Lazy.of(() -> mapper.apply(this.get()).get());
	}

	/** 过滤操作，返回结果仍为延迟初始化对象 */
	public Lazy<Optional<T>> filter(Predicate<? super T> predicate) {
		return Lazy.of(() -> Optional.ofNullable(this.get()).filter(predicate));
	}

	/** 消费操作 */
	public void consume(Consumer<? super T> consumer) {
		consumer.accept(this.get());
	}

	public boolean isInitialized() {
		return initialized;
	}

	public static <T> Lazy<T> of(Supplier<T> supplier) {
		return new Lazy<>(supplier);
	}
}
