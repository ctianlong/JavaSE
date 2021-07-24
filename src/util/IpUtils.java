package util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author ctl
 * @date 2021/7/13
 */
public class IpUtils {

	public static void main(String[] args) {
		System.out.println(ping01("bbb.com"));
	}

	// 第一种实现方式: 使用正则表达式
	public static boolean isValidIpAddressV1(String ipAddress) {
		if (StringUtils.isBlank(ipAddress)) return false;
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		return ipAddress.matches(regex);
	}
	// 第二种实现方式: 使用现成的工具类
	public static boolean isValidIpAddressV2(String ipAddress) {
		if (StringUtils.isBlank(ipAddress)) return false;
		String[] ipUnits = StringUtils.split(ipAddress, '.');
		if (ipUnits.length != 4) {
			return false;
		}
		for (int i = 0; i < 4; ++i) {
			int ipUnitIntValue;
			try {
				ipUnitIntValue = Integer.parseInt(ipUnits[i]);
			} catch (NumberFormatException e) {
				return false;
			}
			if (ipUnitIntValue < 0 || ipUnitIntValue > 255) {
				return false;
			}
			if (i == 0 && ipUnitIntValue == 0) {
				return false;
			}
		}
		return true;
	}
	// 第三种实现方式: 不使用任何工具类
	public static boolean isValidIpAddressV3(String ipAddress) {
		char[] ipChars = ipAddress.toCharArray();
		int length = ipChars.length;
		int ipUnitIntValue = -1;
		boolean isFirstUnit = true;
		int unitsCount = 0;
		for (int i = 0; i < length; ++i) {
			char c = ipChars[i];
			if (c == '.') {
				if (ipUnitIntValue < 0 || ipUnitIntValue > 255) return false;
				if (isFirstUnit && ipUnitIntValue == 0) return false;
				if (isFirstUnit) isFirstUnit = false;
				ipUnitIntValue = -1;
				unitsCount++;
				continue;
			}
			if (c < '0' || c > '9') {
				return false;
			}
			if (ipUnitIntValue == -1) ipUnitIntValue = 0;
			ipUnitIntValue = ipUnitIntValue * 10 + (c - '0');
		}
		if (ipUnitIntValue < 0 || ipUnitIntValue > 255) return false;
		if (unitsCount != 3) return false;
		return true;
	}

	// 注意超时时间，win和linux的ping 命令的超时时间设置参数是不一样的。。。
	public static boolean ping01(String ipAddress) {
		boolean status;
		try {
			int timeOut = 3000;  //超时应该在3钞以上
			status = InetAddress.getByName(ipAddress).isReachable(timeOut); // 当返回值是true时，说明host是可用的，false则不可。
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return status;
	}

	public static boolean ping(String ip) {
		boolean res = false; // 结果
		Runtime runtime = Runtime.getRuntime(); // 获取当前程序的运行进对象
		Process process = null; // 声明处理类对象
		String line = null; // 返回行信息
		InputStream is = null; // 输入流
		InputStreamReader isr = null; // 字节流
		BufferedReader br = null;
		try {
			process = runtime.exec("ping " + ip); // PING
			is = process.getInputStream(); // 实例化输入流
			isr = new InputStreamReader(is, "gbk"); // 把输入流转换成字节流,传入参数为了解决"gbk"中文乱码问题
			br = new BufferedReader(isr); // 从字节中读取文本
			while ((line = br.readLine()) != null) {
				line = new String(line.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
				if (line.contains("TTL") || line.contains("ttl")) { // 通了
					res = true;
					break;
				}
			}
			is.close();
			isr.close();
			br.close();
		} catch (Exception e) {
			System.out.println(e);
			runtime.exit(1);
		}
		return res;
	}
}
