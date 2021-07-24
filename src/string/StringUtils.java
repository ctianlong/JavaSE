package string;

import java.util.OptionalInt;

/**
 * @author ctl
 * @date 2021/7/24
 */
public class StringUtils {

	/**
	 * 准确返回Unicode字符数量的方式，考虑Unicode非基本平面字符
	 */
	public static int getActualLength(String s) {
		if (s == null) {
			return 0;
		}
		return s.codePointCount(0, s.length());
		// 第二种方式
//		 return (int) s.codePoints().count();
	}

	/**
	 * 获取指定位置的单个Unicode字符，考虑Unicode非基本平面字符
	 * @param index 从0开始
	 */
	public static String getCharacterAt(String s, int index) {
		OptionalInt codePointOption = s.codePoints().skip(index).findFirst();
		if (!codePointOption.isPresent()) {
			throw new IndexOutOfBoundsException("character index out of bound");
		}
		int codePointDecimal = codePointOption.getAsInt();
		return new String(new int[]{codePointDecimal}, 0, 1);
	}
}
