package util.zip;

import com.google.common.collect.Maps;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ctl
 * @date 2021/10/14
 */
public class Zip4JTest {

	@Test
	public void extract() throws Exception {
		Map<String, InputStream> map1 = Maps.newHashMap();
		String filePath = "C:\\Users\\ctl\\Desktop\\test1.zip";
		ZipFile zipFile = new ZipFile(filePath);
		List<FileHeader> fileHeaders = zipFile.getFileHeaders();
		for (FileHeader fileHeader : fileHeaders) {
			System.out.println(fileHeader.getFileName());
			System.out.println(getFileName(fileHeader));
			System.out.println(fileHeader.getUncompressedSize());
			System.out.println(fileHeader.isFileNameUTF8Encoded());
			ZipInputStream inputStream = zipFile.getInputStream(fileHeader);
			map1.put(fileHeader.getFileName(), inputStream);
			System.out.println(IOUtils.toByteArray(inputStream).length);
		}
//		System.out.println("------");
//		map1.forEach((key, value) -> {
//			System.out.println(key);
//			try {
//				System.out.println(IOUtils.toByteArray(value).length);
//			} catch (IOException e) {
//			}
//		});
	}

	@Test
	public void extractWithZipInputStream() throws IOException {
		String filePath = "C:\\Users\\ctl\\Desktop\\test1.zip";
		LocalFileHeader localFileHeader;
		byte[] readBuffer = new byte[4096];

		InputStream inputStream = new FileInputStream(filePath);
		try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
			while ((localFileHeader = zipInputStream.getNextEntry()) != null) {
				if (localFileHeader.isDirectory()) {
					continue;
				}
				System.out.println(localFileHeader.getFileName());
				System.out.println(getFileName(localFileHeader));
				System.out.println(localFileHeader.getUncompressedSize());
				System.out.println(localFileHeader.isFileNameUTF8Encoded());

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int len;
				while ((len = zipInputStream.read(readBuffer)) > 0) {
					bos.write(readBuffer, 0, len);
				}
				System.out.println(bos.toByteArray().length);
			}
		}
	}

	/**
	 * @see net.lingala.zip4j.headers.HeaderUtil#decodeStringWithCharset
	 */
	public static String getFileName(FileHeader fileHeader) {
		// 目前压缩包主要是两种来源WINdows和Linux
		if (fileHeader.isFileNameUTF8Encoded()) {
			// 若为true，则返回的fileName已经是UTF8编码的字符串，直接获取即可
			return fileHeader.getFileName();
		} else {
			// 若为false，则返回的fileName是Cp437编码，需要转换为GBK
			return new String(fileHeader.getFileName().getBytes(Charset.forName(InternalZipConstants.ZIP_STANDARD_CHARSET_NAME)), Charset.forName("GBK"));
		}
	}

	public static String getFileName(LocalFileHeader fileHeader) {
		// 目前压缩包主要是两种来源WINdows和Linux
		if (fileHeader.isFileNameUTF8Encoded()) {
			return fileHeader.getFileName();
		} else {
			return new String(fileHeader.getFileName().getBytes(Charset.forName(InternalZipConstants.ZIP_STANDARD_CHARSET_NAME)), Charset.forName("GBK"));
		}
	}

	@Test
	public void testDoubleChange() {
		String s = "我的";
		byte[] gbks = s.getBytes(Charset.forName("GBK"));
		System.out.println(Arrays.toString(gbks));
//		String ss = new String(gbks, Charset.forName("GBK"));
//		System.out.println(ss);

		String s1 = new String(gbks, Charset.forName(InternalZipConstants.ZIP_STANDARD_CHARSET_NAME));
		byte[] bytes = s1.getBytes(Charset.forName(InternalZipConstants.ZIP_STANDARD_CHARSET_NAME));
		System.out.println(Arrays.toString(bytes)); // bytes和gbks相同

		String s2 = new String(gbks, Charset.forName("UTF-8"));
		byte[] bytes2 = s1.getBytes(Charset.forName("UTF-8"));
		System.out.println(Arrays.toString(bytes2)); // bytes2和gbks不相同

	}

}
