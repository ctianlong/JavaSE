package wending;

import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.services.nos.model.ObjectMetadata;
import com.netease.cloud.services.nos.transfer.Transfer;
import com.netease.cloud.services.nos.transfer.TransferManager;
import com.netease.cloud.services.nos.transfer.TransferManagerConfiguration;
import com.netease.cloud.services.nos.transfer.Upload;
import com.netease.snailreader.common.util.MimeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import util.PinYinUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

/**
 * @author ctl
 * @date 2021/10/13
 */
public class NosUploadTool {

	/**
	 * nos.access.key=c86d8c1fa14d4e2688c19801020183f8
	 * nos.secret.key=6077074920a246dcbf72ae85ebf5f739
	 * nos.public.bucket=easyreadfs
	 * nos.base.url=https://${nos.public.bucket}.nosdn.127.net/
	 */

	public static final int MIN_LARGE_OBJECT_SIZE = 50 * 1024 * 1024;
	public static final int MIN_PART_SIZE = MIN_LARGE_OBJECT_SIZE / 2;

	private static String publicBucket = "easyreadfs";
	private static TransferManager transferManager;

	static {
		TransferManagerConfiguration configuration = new TransferManagerConfiguration();
		configuration.setMinimumUploadPartSize(MIN_PART_SIZE);
		configuration.setMultipartUploadThreshold(MIN_PART_SIZE);
		transferManager = new TransferManager(
				new BasicCredentials("c86d8c1fa14d4e2688c19801020183f8", "6077074920a246dcbf72ae85ebf5f739"));
		transferManager.setConfiguration(configuration);
	}

	@Test
	public void upload() {
		String keyPrefix = "qa-visual-config-20211013/";
		Collection<File> files = FileUtils.listFiles(new File("D:\\Tianlong\\Downloads\\preview"), null, false);
		for (File file : files) {
			try {
				String fileName = file.getName();
				if (fileName.contains("、")) {
					fileName = StringUtils.substringAfter(fileName, "、");
				}
				FileInputStream fin = new FileInputStream(file);

				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(fin.available());
				String contentType = MimeUtil.detectMimeType(fileName);
				metadata.setContentType(contentType);

				String key = keyPrefix + PinYinUtil.getPingYin(fileName);

				putObject(publicBucket, key, fin, metadata);

				System.out.println(fileName + ": " + getPublicObjectUrlFromKey(key));
			} catch (Exception e) {
				System.out.println("error: " + file.getName());
				e.printStackTrace();
				break;
			}
		}
	}

	public boolean putObject(String bucketName, String key, InputStream in, ObjectMetadata metadata) {
		boolean succ = false;
		try {
			Upload upload = transferManager.upload(bucketName, key, in, metadata);
			upload.waitForCompletion();
			succ = upload.getState() == Transfer.TransferState.Completed;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return succ;
	}

	public static String getPublicObjectUrlFromKey(String key) {
		return "https://easyreadfs.nosdn.127.net/" + key;
	}

}
