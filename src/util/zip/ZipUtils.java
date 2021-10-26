package util.zip;

import com.google.common.collect.Maps;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.util.InternalZipConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author ctl
 * @date 2021/10/18
 */
public class ZipUtils {

	/**
	 * 解压文件，只提取文件，忽略目录类型
	 * @param filePath 压缩文件所在路径
	 * @return 每个文件的名称和输入流
	 * @throws IOException
	 */
	public static Map<String, InputStream> extract(String filePath) throws IOException {
		Map<String, InputStream> result = Maps.newLinkedHashMap();
		ZipFile zipFile = new ZipFile(filePath);
		List<FileHeader> fileHeaders = zipFile.getFileHeaders();
		for (FileHeader fileHeader : fileHeaders) {
			if (fileHeader.isDirectory()) {
				continue;
			}
			ZipInputStream inputStream = zipFile.getInputStream(fileHeader);
			result.put(getFileName(fileHeader), inputStream);
		}
		return result;
	}

	/**
	 * 解压文件，只提取文件，忽略目录类型
	 * @param inputStream 压缩文件输入流
	 * @return 每个文件的名称和内容字节数组
	 * @throws IOException
	 */
	public static Map<String, byte[]> extract(InputStream inputStream) throws IOException {
		Map<String, byte[]> result = Maps.newLinkedHashMap();
		LocalFileHeader localFileHeader;
		byte[] readBuffer = new byte[4096];
		try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
			while ((localFileHeader = zipInputStream.getNextEntry()) != null) {
				if (localFileHeader.isDirectory()) {
					continue;
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int len;
				while ((len = zipInputStream.read(readBuffer)) > 0) {
					bos.write(readBuffer, 0, len);
				}
				result.put(getFileName(localFileHeader), bos.toByteArray());
			}
		}
		return result;
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

}
