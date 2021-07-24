package wending;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import util.http.HttpClientUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ctl
 * @date 2021/7/9
 */
public class WdBatchDeleteArticle {

	@Test
	public void wdBatchDeleteArticle() throws InterruptedException {
		String[] split = StringUtils.split(bookIds, ",");
		List<List<String>> partition = Lists.partition(Lists.newArrayList(split), 100);
		int count = 0;
		String sourceUuid = "";
		for (List<String> articleUuids : partition) {
			try {
				invoke(sourceUuid, articleUuids);
			} catch (Exception e) {
				System.out.println("exception: " + articleUuids.get(0));
				e.printStackTrace();
			}
			count += articleUuids.size();
			System.out.println("finished: " + count);
			Thread.sleep(5000L);
		}
		System.out.println("------end");
	}

	public boolean invoke(String sourceUuid, List<String> articleUuids) throws Exception {
		CloseableHttpClient httpClient = HttpClientUtils.getDefaultHttpClient();
		Map<String, String> header = new HashMap<>();
		header.put("Cookie", "NOVEL_FINANCE_BACKEND_U=c2b49511-cecc-499e-aac8-31feaa95ebf0-1625647491736; _us.c.v.online=CgEmNl8ScjAeBR4XAjkpCVpSBkFLREteZg9ceFhcXxdIcVwaVgJPelVPTQFASw614ab32b;");
		Map<String, String> params = new HashMap<>();
		params.put("bookId", sourceUuid);
		params.put("articleIds", StringUtils.join(articleUuids, ","));
		params.put("status", "-1");
		params.put("csrf_token", "7cd16acda63bf605c9ccb1e61dcd1f92");
		HttpClientUtils.Response response = HttpClientUtils.post("https://wending.hz.netease.com/book/mirror/setChargeStatus.json", header, params, null, httpClient);
		if (Objects.equals(response.getStatusCode(), 200)) {
			JSONObject jsonObject = JSONObject.parseObject(response.getResponseBody());
			Integer code = jsonObject.getInteger("code");
			if (Objects.equals(code, 0)) {
				return true;
			}
		}
		System.out.println("apiError: " + articleUuids.get(0));
		System.out.println(response);
		return false;
	}

	private String bookIds = "";
}
