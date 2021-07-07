package util.version;

/**
 * 版本比较工具，适用于版本号格式为：x.x.x，其中x为数字，x的个数不限（即不限于3个数字）
 * @author ctl
 * @date 2021/7/2
 */
public class VersionUtils {

	public static boolean gt(String v1, String v2) {
		int compare = compare(v1, v2);
		return compare == 1;
	}

	public static boolean gte(String v1, String v2) {
		int compare = compare(v1, v2);
		return compare == 1 || compare == 0;
	}

	public static boolean lt(String v1, String v2) {
		int compare = compare(v1, v2);
		return compare == -1;
	}

	public static boolean lte(String v1, String v2) {
		int compare = compare(v1, v2);
		return compare == -1 || compare == 0;
	}

	/**
	 * 版本比较
	 * @return 1:v1>v2, -1:v1<v2, 0:v1=v2
	 */
	public static int compare(String v1, String v2) {
		String[] thisParts = v1.split("\\.");
		String[] thatParts = v2.split("\\.");
		int length = Math.max(thisParts.length, thatParts.length);
		for (int i = 0; i < length; i++) {
			int thisPart = i < thisParts.length ?
					Integer.parseInt(thisParts[i]) : 0;
			int thatPart = i < thatParts.length ?
					Integer.parseInt(thatParts[i]) : 0;
			if (thisPart < thatPart)
				return -1;
			if (thisPart > thatPart)
				return 1;
		}
		return 0;
	}

}
