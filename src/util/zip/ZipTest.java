package util.zip;

import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * https://www.baeldung.com/java-compress-and-uncompress
 * @author ctl
 * @date 2021/10/14
 */
public class ZipTest {

	@Test
	public void listFileFromZip() throws IOException {
		Map<String, byte[]> files = Maps.newHashMap();
		String fileZip = "C:\\Users\\ctl\\Desktop\\Desktop.zip";
		byte[] buffer = new byte[1024 * 4];
		// 1、Windows下使用WinRAR、好压等工具压缩的文件 特点：文件名为GBK编码
		// 2、使用Linux、MacOS等系统压缩的zip文件 特点：文件名为UTF-8编码
		Charset charset = Charset.forName("GBK");
		ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip), charset);
		ZipEntry zipEntry;
		while ((zipEntry = zis.getNextEntry()) != null) {
			System.out.println(zipEntry.getName());
			if (zipEntry.isDirectory()) {
				System.out.println("is directory");
				continue;
			}
			System.out.println(zipEntry.getSize());



			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			while ((len = zis.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			files.put(zipEntry.getName(), bos.toByteArray());

//			byte[] data = new byte[(int) zipEntry.getSize()];
//			zis.read(data);
//			files.put(zipEntry.getName(), data);
		}
		zis.closeEntry();
		zis.close();

//		System.out.println("------");
//		files.entrySet().forEach(e -> {
//			System.out.println(e.getKey());
//			System.out.println(e.getValue().length);
//		});
	}

	@Test
	public void unZip() throws IOException {
		String fileZip = "C:\\Users\\ctl\\Desktop\\Desktop.zip";
		File destDir = new File("C:\\Users\\ctl\\Desktop\\新建文件夹");
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry);
			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}
}
