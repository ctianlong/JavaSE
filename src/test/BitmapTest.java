package test;

import org.junit.Test;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.buffer.ImmutableRoaringBitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author ctl
 * @date 2022/3/10
 */
public class BitmapTest {

	@Test
	public void test() throws IOException {
		RoaringBitmap map = new RoaringBitmap();
		for (int i = 1; i <= 2000; i++) {
			if (i % 2 == 0) {
				continue;
			}
//			if (i >= 1000 && i <= 2000) {
//				continue;
//			}
//			if (Math.random() > 0.5) {
//				continue;
//			}
			map.add(i);
		}
		System.out.println(map.getCardinality());
		System.out.println(map.contains(1501));
		System.out.println(map.contains(5000));

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream wheretoserialize = new DataOutputStream(out);
		map.runOptimize(); // can help compression
		map.serialize(wheretoserialize);
		byte[] bin = out.toByteArray();
		System.out.println("压缩前：" + bin.length);
		byte[] compress = compress(bin);
		System.out.println("压缩后：" + compress.length);

		byte[] uncompress = uncompress(compress);

//		byte[] binaryData = out.toByteArray();
//		String b64 = Base64.encodeBase64String(binaryData);
//		System.out.println(b64);
//		System.out.println(b64.length());
//		System.out.println(Hex.encodeHexString(binaryData));


		ByteBuffer bb = ByteBuffer.wrap(uncompress);
		ImmutableRoaringBitmap rrback1 = new ImmutableRoaringBitmap(bb);
		System.out.println(rrback1.getCardinality());
		System.out.println(rrback1.contains(1501));
		System.out.println(rrback1.contains(5000));


	}

	public static byte[] compress(byte[] binaryInput) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
		gzipOut.write(binaryInput);
		gzipOut.finish();
		gzipOut.close();
		return baos.toByteArray();
	}

	public static byte[] uncompress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}


}
