package util.lazy;

/**
 * @author ctl
 * @date 2021/5/12
 */
@FunctionalInterface
public interface ThrowableConsumer<T, X> {

	void accept(T t);
}
