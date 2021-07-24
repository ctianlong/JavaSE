package string;

import org.junit.Test;

/**
 * 汉字Unicode编码范围：https://www.qqxiuzi.cn/zh/hanzi-unicode-bianma.php
 * @author ctl
 * @date 2021/7/24
 */
public class StringUnicodeTest {

	@Test
	public void testUnicodeAndUft16() {
		// abc你好𠀀𠀁𠀂𠀃   注意：后4个汉字非基本平面，需要用2个char，即4个字节来表示
		// 实际字符长度应该是9，String.length()方法返回的是utf16转换格式下的码元长度，也就是char[]的长度
		// utf16一个码元长度是16位，即2个字节，也就是一个char占用的空间
		String s = "abc你好\uD840\uDC00\uD840\uDC01\uD840\uDC02\uD840\uDC03";
		// abc你好𠀀𠀁𠀂𠀃
		System.out.println(s);
		// 返回的是utf16转换格式下的码元长度，也就是char[]的长度。
		// 若存在非Unicode基本平面的字符（不能用utf16下一个码元，即2个字节来表示），则返回的长度和实际包含的字符数量是不一样的
		System.out.println(s.length());
		// 准确返回字符数量的方式
		System.out.println(s.codePoints().count());
		System.out.println(s.codePointCount(0, s.length()));
		// 返回char[]数组中对应下标的元素，即返回一个码元，当该码元对应基本平面中的一个码点时，正常显示。
		// 若存在非基本平面字符，需要2个码元（即代理对）表示，则单独取出代理对中的其中一个码元便是乱码（用问号显示）
		// 因此，该方法无法准确获取第几个字符（字符即码点，即肉眼可见的一个符号）。
		System.out.println(s.charAt(5));
		// 注意：下述方法只有指向代理对中的高半区的char时才能返回代理对所对应的码点的十进制数字表示。
		// 若指向低半区的char，则直接返回那个char对应的十进制数字。
		// 因此，该方法不适合实现准确获取第几个字符，因此index无法准确定位。
		System.out.println(s.codePointAt(5));
		// 注意：与codePointAt相反，若index-1所指向的char是低半区，则正确返回代理对所对应的码点的十进制数字表示。
		// 若index-1指向高半区的char，则直接返回那个char对应的十进制数字。
		System.out.println(s.codePointBefore(7));
		// 下述方式可以准确获取第几个字符！！！
		int codePointDecimal = s.codePoints().skip(3).findFirst().getAsInt();
		System.out.println(codePointDecimal);
		String shot = new String(new int[]{codePointDecimal}, 0, 1);
		System.out.println(shot);
	}

	@Test
	public void testSubString() {
		// abc你好𠀀𠀁𠀂𠀃   注意：后4个汉字非基本平面，需要用2个char，即4个字节来表示
		String s = "abc你好\uD840\uDC00\uD840\uDC01\uD840\uDC02\uD840\uDC03";
		// 注意，用 substring 也无法准确获取第几个字符，substring本质上是对char[]中的部分复制，针对char维度操作，不考虑代理对情况
		// 下述参数会返回乱码
		System.out.println(s.substring(5, 6));
	}





}
