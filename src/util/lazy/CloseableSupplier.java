package util.lazy;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

/**
 * @author ctl
 * @date 2021/5/12
 */
public class CloseableSupplier<T> implements Supplier<T>, Serializable {
	private static final long serialVersionUID = 0L;
	private final Supplier<T> delegate;
	private final boolean resetAfterClose;
	private volatile transient boolean initialized;
	private transient T value;

	private CloseableSupplier(Supplier<T> delegate, boolean resetAfterClose) {
		this.delegate = delegate;
		this.resetAfterClose = resetAfterClose;
	}

	@Override
	public T get() {
		// 经典Singleton实现
		if (!(this.initialized)) { // 注意是volatile修饰的，保证happens-before，t一定实例化完全
			synchronized (this) {
				if (!(this.initialized)) { // Double Lock Check
					T t = this.delegate.get();
					this.value = t;
					this.initialized = true;
					return t;
				}
			}
		}
		// 初始化后就直接读取值，不再同步抢锁
		return this.value;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public <X extends Throwable> void ifPresent(ThrowableConsumer<T, X> consumer) throws X {
		synchronized (this) {
			if (initialized && this.value != null) {
				consumer.accept(this.value);
			}
		}
	}

	public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
		checkNotNull(mapper);
		synchronized (this) {
			if (initialized && this.value != null) {
				return ofNullable(mapper.apply(value));
			} else {
				return empty();
			}
		}
	}

	private <U> void checkNotNull(Function<? super T, ? extends U> mapper) {
		if (mapper == null) {
			throw new IllegalArgumentException("mapper is null");
		}
	}

	public void tryClose() {
		tryClose(i -> { });
	}

	public <X extends Throwable> void tryClose(ThrowableConsumer<T, X> close) throws X {
		synchronized (this) {
			if (initialized) {
				close.accept(value);
				if (resetAfterClose) {
					this.value = null;
					initialized = false;
				}
			}
		}
	}

	public String toString() {
		if (initialized) {
			return "MoreSuppliers.lazy(" + get() + ")";
		} else {
			return "MoreSuppliers.lazy(" + this.delegate + ")";
		}
	}


}
