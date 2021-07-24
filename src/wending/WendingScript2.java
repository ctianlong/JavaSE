package wending;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import util.http.HttpClientUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author ctl
 * @date 2020/6/30
 */
public class WendingScript2 {

//    @Test
//    public void batchAddArticle() {
//        CloseableHttpClient httpClient = HttpClientUtils.getDefaultHttpClient();
//        Map<String, String> header = new HashMap<>();
//        header.put("Cookie", "_us.c.v.test=dlcvVTYBZRx1GEcxXjcYC1pSBkNOQ0tVbQ9QdlVVXxdIcVwaVgJPelVPTQFASwb07dcfc6; NOVEL_FINANCE_BACKEND_U=4dfe5f5c-f42d-460e-9378-843a0c1486b3-1593577791482");
//        Map<String, String> params = new HashMap<>();
//        params.put("articleContent", "<p>d歌</p>");
//        params.put("articleTitle", "测试标题aaa");
//        params.put("indexId", "1339");
//        params.put("bookId", "ts_cfb9fa00744348ea9d425149fd40c254_4");
//        params.put("needPay", "1");
//        params.put("needAnti", "false");
//        params.put("csrf_token", "3313fbcb5e0654489117eeed22498652");
//        try {
//            HttpClientUtils.Response response = HttpClientUtils.post("https://twending.hz.netease.com/book/mirror/saveNewArticle.json", header, params, null, httpClient);
//            System.out.println(response);
//        } catch (IOException e) {
//            System.out.println("[error]" + e.getMessage());
//        }
//    }

    private static boolean apiInsert(String title, String content, long index) throws Exception {
        CloseableHttpClient httpClient = HttpClientUtils.getDefaultHttpClient();
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "NOVEL_FINANCE_BACKEND_U=d85ea5d1-68ba-486c-a770-406b85785029-1600392963477");
        Map<String, String> params = new HashMap<>();
        params.put("articleContent", content);
        params.put("articleTitle", title);
        params.put("indexId", String.valueOf(index));
        params.put("bookId", "fb76db29575346028064c958bee7d44b_4");
        params.put("needPay", "1");
        params.put("needAnti", "false");
//        params.put("csrf_token", "3313fbcb5e0654489117eeed22498652");
        HttpClientUtils.Response response = HttpClientUtils.post("https://wending.hz.netease.com/book/mirror/saveNewArticle.json", header, params, null, httpClient);
        if (Objects.equals(response.getStatusCode(), 200)) {
	        JSONObject jsonObject = JSONObject.parseObject(response.getResponseBody());
	        Integer code = jsonObject.getInteger("code");
	        if (Objects.equals(code, 0)) {
	        	return true;
	        }
        }
	    System.out.println("insert error: " + title);
	    System.out.println(response);
        return false;
    }

    @Test
    public void read() throws Exception {
	    System.out.println("startTime: " + new Date());
        // 注意首行的标题可能有特殊字符，导致无法匹配正则
        String path = "C:\\Users\\ctl\\Desktop\\文档1-修改.txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        int count = 0;
        int count1 = 0;
        List<String> error = Lists.newArrayList();
        int startIndex = 811;
        long startSort = 41550051;
        String realTitle = null;
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line == null) {
                // 文件已经读完，进行最后一章节插入，然后跳出整个循环
                if (realTitle != null && sb.length() > 0) {
                	long t1 = System.currentTimeMillis();
	                try {
	                	if (!apiInsert(realTitle, sb.toString(), startSort)) {
	                		break;
		                }
	                } catch (Exception e) {
		                System.out.println("insert exception: " + realTitle);
		                e.printStackTrace();
		                break;
	                }
	                count1++;
	                long consume = System.currentTimeMillis() - t1;
	                System.out.println(count1 + " | " + realTitle + " | " + startSort + " | " +  consume + "ms");
//	                System.out.println(sb.toString());
                }
                break;
            }
            if (StringUtils.isBlank(line)) {
                continue;
            }
            line = line.trim();
            line = line.replaceFirst("\\u3000", "");
            if (isTitle(line)) {
                // 将上一次存储的章节插入
	            if (realTitle != null && sb.length() == 0) {
	            	error.add(line);
	            }
                if (realTitle != null && sb.length() > 0) {
	                long t1 = System.currentTimeMillis();
	                try {
		                if (!apiInsert(realTitle, sb.toString(), startSort)) {
			                break;
		                }
	                } catch (Exception e) {
		                System.out.println("insert exception: " + realTitle);
		                e.printStackTrace();
		                break;
	                }
	                count1++;
	                long consume = System.currentTimeMillis() - t1;
	                System.out.println(count1 + " | " + realTitle + " | " + startSort + " | " +  consume + "ms");
//	                System.out.println(sb.toString());
//	                Thread.sleep(500L);
                    startSort += 50;
	                startIndex++;
                }
//	            System.out.println(line);
                String[] split = StringUtils.split(line, "：");
                realTitle = "第" + String.valueOf(startIndex) + "章";
                if (split.length == 2) {
//                    realTitle = realTitle + "：" + split[1];
                } else {
                    error.add(line);
                }
                sb.delete(0, sb.length());
                count++;
            } else {
                sb.append("<p>").append(line).append("</p>");
            }
        }
        System.out.println(count);
        System.out.println(count1);
        System.out.println(error);
    }

//    private static final Pattern PATTERN = Pattern.compile("^\\d+$");
    private static final Pattern PATTERN1 = Pattern.compile("^\\d+[:：].*$");
    private static final Pattern PATTERN2 = Pattern.compile("^第?[零|一|二|三|四|五|六|七|八|九|十|百|千]+章：.*$");
    private static final Pattern PATTERN4 = Pattern.compile("^第?[零|一|二|三|四|五|六|七|八|九|十|百|千]+章\\s.*$");
    private static final Pattern PATTERN3 = Pattern.compile("^第\\d+章\\s.*$");

    // 1开棺有喜：冥夫求放过 + 2恐怖女网红(1)
    private static final Pattern P1 = Pattern.compile("^(第|(番外))?[\\d-]+章?\\s.*$");

    private static boolean isTitle(String line) {
        return PATTERN2.matcher(line).matches();
    }

    @Test
	public void testddd() {
    	String d = "　第三百二十八章：叱咤风云";
    	d = d.trim();
    	d = d.replaceFirst("\\u3000", "");
	    System.out.println(">>>" + d);
    }

}
