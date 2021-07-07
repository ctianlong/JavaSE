package util.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author ctl
 * @date 2021/7/2
 */
public class Version implements Comparable<Version> {

	private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)*");

	private final String version;

	public final String get() {
		return this.version;
	}

	public Version(String version) {
		if (version == null)
			throw new IllegalArgumentException("Version can not be null");
		if (!VERSION_PATTERN.matcher(version).matches())
			throw new IllegalArgumentException("Invalid version format");
		this.version = version;
	}

	@Override
	public int compareTo(Version that) {
		if (that == null)
			return 1;
		return VersionUtils.compare(this.get(), that.get());
	}

	@Override
	public boolean equals(Object that) {
		if (this == that)
			return true;
		if (that == null)
			return false;
		if (this.getClass() != that.getClass())
			return false;
		return this.compareTo((Version) that) == 0;
	}

	public static void main(String[] args) {
		Version a = new Version("1.1");
		Version b = new Version("1.1.1");
		System.out.println(a.compareTo(b)); // return -1 (a<b)
		System.out.println(a.equals(b));;    // return false

		a = new Version("2.0");
		b = new Version("1.9.9");
		System.out.println(a.compareTo(b));; // return 1 (a>b)
		System.out.println(a.equals(b));;    // return false

		a = new Version("1.0");
		b = new Version("1");
		System.out.println(a.compareTo(b));; // return 0 (a=b)
		System.out.println(a.equals(b));;    // return true

		a = new Version("1");
		b = null;
		System.out.println(a.compareTo(b));; // return 1 (a>b)
		System.out.println(a.equals(b));;    // return false

		List<Version> versions = new ArrayList<Version>();
		versions.add(new Version("2"));
		versions.add(new Version("1.0.5"));
		versions.add(new Version("1.01.0"));
		versions.add(new Version("1.00.1"));
		System.out.println(Collections.min(versions).get()); // return min version
		System.out.println(Collections.max(versions).get()); // return max version

		// WARNING
		a = new Version("2.06");
		b = new Version("2.060");
		System.out.println(a.equals(b));;    // return false
	}

}
