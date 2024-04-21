package example;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 文件分片
 * @author ctl
 * @createTime 2023/05/15 16:46
 * @description
 */
public class FileChunkExample {

    @Test
    public void fromFile() {
        String filePath = "/path/to/your/file"; // 设置要读取的文件路径
        int chunkSize = 5 * 1024 * 1024; // 分片大小为5MB

        try (InputStream inputStream = new FileInputStream(filePath)) {
            ByteArrayOutputStream outputStream;
            byte[] buffer = new byte[chunkSize];
            int bytesRead;

            int chunkIndex = 1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream = new ByteArrayOutputStream(bytesRead);
                outputStream.write(buffer, 0, bytesRead);

                // 在这里处理当前分片的数据，可以将其写入其他文件、进行处理等
                processChunk(outputStream.toByteArray(), chunkIndex);

                chunkIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fromDataArray() throws IOException {
        byte[] fileData = getFileData(); // 获取文件数据的字节数组
        int chunkSize = 5 * 1024 * 1024; // 分片大小为5MB

        int startIndex = 0;
        int chunkIndex = 1;

        while (startIndex < fileData.length) {
            int endIndex = Math.min(startIndex + chunkSize, fileData.length);
            byte[] chunkData = Arrays.copyOfRange(fileData, startIndex, endIndex);

            // 在这里处理当前分片的数据，可以将其写入其他文件、进行处理等
            processChunk(chunkData, chunkIndex);

            startIndex = endIndex;
            chunkIndex++;
        }

        byte[] data = mergeByteArrays(arrays);
        IOUtils.write(data, Files.newOutputStream(Paths.get("/Users/chengtianlong/Desktop/1684134925839-1.mp4")));
    }

    @Test
    public void fromUrlInputStream() throws IOException {
        String fileUrl = "https://easyreadfs.nosdn.127.net/1684134925839.mp4"; // 设置文件的URL地址
        int chunkSize = 5 * 1024 * 1024; // 分片大小为5MB

        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream inputStream = connection.getInputStream()) {
                ByteArrayOutputStream outputStream;
                byte[] buffer = new byte[chunkSize];
                int totalBytesRead = 0;
                int bytesRead;
                int chunkIndex = 1;

                // 对于从网络中读取文件并进行分片处理，需要处理inputStream.read(buffer)返回的读取字节数量bytesRead小于分片大小的情况。这是因为网络数据的到达速度和读取速度可能不完全匹配。
                // 使用了一个totalBytesRead变量来跟踪读取的总字节数，并在每次读取完整个分片大小时进行处理。如果读取的字节数小于分片大小，则继续累加totalBytesRead，直到达到分片大小或读取完文件。
                while ((bytesRead = inputStream.read(buffer, totalBytesRead, buffer.length - totalBytesRead)) != -1) {
                    totalBytesRead += bytesRead;

                    if (totalBytesRead == buffer.length) {
                        outputStream = new ByteArrayOutputStream(buffer.length);
                        outputStream.write(buffer, 0, buffer.length);

                        // 在这里处理当前分片的数据，可以将其写入其他文件、进行处理等
                        // todo 注意：从url流中分批获取，如果每个批次处理逻辑耗时太长，会造成url请求超时
                        processChunk(outputStream.toByteArray(), chunkIndex);

                        totalBytesRead = 0;
                        chunkIndex++;
                    }
                }

                // 处理最后一个分片（如果存在）
                if (totalBytesRead > 0) {
                    outputStream = new ByteArrayOutputStream(totalBytesRead);
                    outputStream.write(buffer, 0, totalBytesRead);

                    // 在这里处理当前分片的数据，可以将其写入其他文件、进行处理等
                    processChunk(outputStream.toByteArray(), chunkIndex);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] data = mergeByteArrays(arrays);
        IOUtils.write(data, Files.newOutputStream(Paths.get("/Users/chengtianlong/Desktop/1684134925839-1.mp4")));
    }

    private static byte[] getFileData() throws IOException {
        // 返回文件数据的字节数组，可以根据实际情况获取文件数据
        // 示例中直接返回一个字节数组作为示范
//        return new byte[] { /* 文件数据 */ };
        return IOUtils.toByteArray(new URL("https://easyreadfs.nosdn.127.net/1684134925839.mp4"));
    }

    private static List<byte[]> arrays = Lists.newArrayList();

    private static void processChunk(byte[] chunkData, int chunkIndex) {
//        try {
//            Thread.sleep(200 * 1000L);
//        } catch (InterruptedException e) {
//        }
        // 在这里处理当前分片的数据
        System.out.println("Chunk " + chunkIndex + ": " + chunkData.length + " bytes");
        arrays.add(chunkData);
    }

    private static byte[] mergeByteArrays(List<byte[]> arrays) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (byte[] array : arrays) {
            try {
                outputStream.write(array);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return outputStream.toByteArray();
    }


}
