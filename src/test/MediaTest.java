package test;

import org.apache.tika.Tika;
import org.junit.Test;

/**
 * @author ctl
 * @date 2021/10/15
 */
public class MediaTest {

	@Test
	public void videoTest() {
		Tika tika = new Tika();
		String mp4 = tika.detect("aaa.mp4");
		String gp3 = tika.detect("11.3gp");
		String avi = tika.detect("22.avi");
		String mpeg = tika.detect("33.mpeg");
		String jpg = tika.detect("bbb.jpg");
		String png = tika.detect("bbb.png");
		String gif = tika.detect("bbb.gif");
		System.out.println(mp4);
		System.out.println(gp3);
		System.out.println(avi);
		System.out.println(mpeg);
		System.out.println(jpg);
		System.out.println(png);
		System.out.println(gif);
	}
}
