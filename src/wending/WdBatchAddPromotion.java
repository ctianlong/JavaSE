package wending;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import util.http.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author ctl
 * @date 2021/7/1
 */
public class WdBatchAddPromotion {

	/**
	 * 批量添加清水推荐
	 */
	@Test
	public void batchAddQsPromotion() throws InterruptedException {
		String[] split = bookIds.split("\n");
		Set<String> set = Sets.newLinkedHashSet(Lists.newArrayList(split));
//		set = Sets.newHashSet("bd_91b656089eb54e9b93dfcbe58588f059_4");
		int count = 0;
		for (String id : set) {
//			System.out.println(id);
			try {
				boolean succ = invoke(id);
				if (succ) {
					count++;
				}
			} catch (Exception e) {
				System.out.println("error: " + id);
				e.printStackTrace();
			}
			Thread.sleep(50L);
		}
		System.out.println("success: " + count);
	}

	public boolean invoke(String sourceUuid) throws Exception {
		CloseableHttpClient httpClient = HttpClientUtils.getDefaultHttpClient();
		Map<String, String> header = new HashMap<>();
		header.put("Cookie", "NOVEL_FINANCE_BACKEND_U=c2b49511-cecc-499e-aac8-31feaa95ebf0-1625647491736; _us.c.v.online=CgEmNl8ScjAeBR4XAjkpCVpSBkFLREteZg9ceFhcXxdIcVwaVgJPelVPTQFASw614ab32b;");
		Map<String, String> params = new HashMap<>();
		params.put("bookId", sourceUuid);
		params.put("csrf_token", "7cd16acda63bf605c9ccb1e61dcd1f92");
		HttpClientUtils.Response response = HttpClientUtils.post("https://wending.hz.netease.com/promotion/promoteBookBookType.json", header, params, null, httpClient);
		if (Objects.equals(response.getStatusCode(), 200)) {
			JSONObject jsonObject = JSONObject.parseObject(response.getResponseBody());
			Integer code = jsonObject.getInteger("code");
			if (Objects.equals(code, 0)) {
				return true;
			}
		}
		System.out.println("add error: " + sourceUuid);
		System.out.println(response);
		return false;
	}

	public String bookIds = /*"bd_91b656089eb54e9b93dfcbe58588f059_4\n" +*/
			"bd_2b20fefe6df44b2c99ffc468bf29f6ce_4\n" +
			"bd_20c1b02c29194f6bac7b9300f9b9d05c_4\n" +
			"bd_0397c20c589a442ba66de485baf7c629_4\n" +
			"ts_c07eb39b79d44dd0b0db8ca2a14f4b6a_4\n" +
			"bd_d01d7b48238b4f04aedd1172d05e7d20_4\n" +
			"bd_2ad9b6925cda44aa967302b48658c9e9_4\n" +
			"ts_f14c828596294deda7691b36f0a0db0c_4\n" +
			"ts_459869b7e533494f966d5d9e3b2ed6fa_4\n" +
			"ts_7bf3db379e06460f9d9f129a5a4d1fb8_4\n" +
			"5138226662c246f0ba18927855fffb40_4\n" +
			"ts_04a408e1dfa64c2a8e4104261dbf8d39_4\n" +
			"f6f3072cefef4c168a2b6a0a797079f9_4\n" +
			"674915e5bebf4bf8af04302c5886517f_4\n" +
			"a93570b9758643a69adcef20e448635c_4\n" +
			"ts_6daf9c0dbce0443597ab36fe282ea188_4\n" +
			"ts_2125ea6c0c6a4259b2997f4bf93220c7_4\n" +
			"ts_f0afe7b331d040048ff7e067d22d24df_4\n" +
			"ts_7206e9e138a14db4b66d117e51b1e1b0_4\n" +
			"ts_0bf05403d73b4365b34926e92842aee1_4\n" +
			"bd_6400e8e6a8d84e688cfb8b29027ef388_4\n" +
			"bd_2021fb03675e4052a2c2d39af2beceeb_4\n" +
			"bd_a0314a060e79455a9dd5064af2c443aa_4\n" +
			"ts_da3cc02404634761879ba5bd4cf84e84_4\n" +
			"bd_81e88d93a1a041cbab4516297163fa2f_4\n" +
			"bd_ef18f7ef4fc447269eee2607f8199cb7_4\n" +
			"ts_5a0fc0c01f614ca2b1648ace2f2861fb_4\n" +
			"ts_82661f39d83845c8911d70916a9f3a6c_4\n" +
			"bd_03cccd39cbbb446a820ffb0bc63dfc10_4\n" +
			"bd_91f68cff98d24b539504f8958579d359_4\n" +
			"e6ab1d61b216438fb5e37199612d51e5_4\n" +
			"fbe18a137ee447118eadaeac178ba20e_4\n" +
			"ts_915e0d72a05846b08372c2ae9f8203f5_4\n" +
			"ts_082e4596ce954dd3a7ac8bb6ce91a764_4\n" +
			"ts_58b749eaa9d347c88d90a1943feaadd5_4\n" +
			"ts_7b6ce87176554ce9971cc4f1b6438ab0_4\n" +
			"9236c6152f5148aaaa911f121e097713_4\n" +
			"ts_7b0e2bd1c7b24cfc84d78ac343b592ac_4\n" +
			"ts_3d3ae9918ced4ec6bb2f0bb5579de12f_4\n" +
			"ts_624bb003613640c19d6fce50920d0b09_4\n" +
			"ts_70d92e59a3404cf5b67158625f79ef90_4\n" +
			"ts_8e461af35258427bbdb21b20adcd5454_4\n" +
			"ts_35be09ea461d4de3b68cd1a0306324bb_4\n" +
			"ts_c57b4467ba0449f3b941c5e3f0d1064f_4\n" +
			"bd_8b17b438842241aebdf368534231ab05_4\n" +
			"bd_cd79f963679b4a5aa5d6ee49aaccec93_4\n" +
			"bd_d5d3f01149b04ae3abe62c32ed1de99d_4\n" +
			"09bc09ef3acb4e66835001442c5dc2f9_4\n" +
			"ts_ef99c0298aab4edea7df09378fccd693_4\n" +
			"ts_cff5419e821048c6bf93c406e0f6c2b7_4\n" +
			"ts_14b380a3fd084741a96922a34138b938_4\n" +
			"ts_c007c28d54b749459f0544a2f16fe0c9_4\n" +
			"ts_dcdf371169c14f4fafa9a4b1b2c76edf_4\n" +
			"ts_65fc925ba6db47dfac554da8c046d018_4\n" +
			"ts_f01d557fe21049aa94052f5f0e646b4d_4\n" +
			"ts_aa894988dd7344afb176a15663f1ac54_4\n" +
			"ts_c3c07bce25cd4ce6b5aaaf7ded5c8d5c_4\n" +
			"ts_e531eb1d8d6f4237bd06ff485583c811_4\n" +
			"ts_efc31ac154ac48e6adde0ba1d171d513_4\n" +
			"ts_886e99eb45ce48aab71d6e0c9a2d338f_4\n" +
			"ts_2d08c2a6c5c347139fbdac779710ab86_4\n" +
			"ts_41deb2d4c94b4e2a9c8a1ba14e68eb35_4\n" +
			"ts_6bf091f5f1ef45878af4aaabc75397d6_4\n" +
			"ts_d5953352ede7434f8cabb119ccd10718_4\n" +
			"ts_481ba83e2d8c4833bd3071674ee3ecc1_4\n" +
			"ts_2882ebaaf7f8459f8be7e8da9cfa6015_4\n" +
			"ts_058936e9434e438394a61798dc355a3b_4\n" +
			"84baf3637a704a0a80bbb6a7b37c0317_4\n" +
			"552c38dd0e8247a2abdd06a9477f9dbf_4\n" +
			"ts_c08f8e1eb2a4469bbc3c5b57a9f4aebd_4\n" +
			"ts_20ca09e9b6814ab7b8c2779b19270c5b_4\n" +
			"ts_e57ad362da1340b5b1f71649388cc453_4\n" +
			"ts_d10ed5d627904d1cb4b75a44151a21f7_4\n" +
			"3bb55305567642ff9a4b2612ad431783_4\n" +
			"bd_5ffdd9a61b1d4e68a64ef8bb9d4d6f23_4\n" +
			"bd_653f40877dcb45d29b76c48cd292748e_4\n" +
			"2138f19707674f9ba15ba0fddee4cb64_4\n" +
			"10b03807e9f94000bbe03c1a04e93dae_4\n" +
			"d3e062b6e53341f086388b4189096abf_4\n" +
			"b18f9a7a7fe641fc84a41f5c999e8640_4\n" +
			"ts_a3c9595e17ee4607aeacd73bd0e3efb4_4\n" +
			"77bfb13e7b6244f6b4b238cd603a9e13_4\n" +
			"4f0a8488cf434bbbabdd9c561af7a2ad_4\n" +
			"32e7506b1cf742eda3e501dc9d41b3a7_4\n" +
			"e1df638a1f8c4cb69a3cef75c8a15dde_4\n" +
			"ts_fa370c6278bd43e6b8f41b1a22bb098c_4\n" +
			"bd_c5fec24883814926891a4687b35bd57d_4\n" +
			"ts_339333fcf8c74014ac9e474baba252aa_4\n" +
			"8695c15900d24bde8bb26fe3126dfde1_4\n" +
			"bd_eef30539087d4a54b48f680cec9afef4_4\n" +
			"ts_91d0118be9a7429aaa7854bab46932ac_4\n" +
			"bd_6829dcb951864eda8fc06197fa48cb0a_4\n" +
			"bd_cda883dad6574722a6b863d05fd42f25_4\n" +
			"ts_7d6c30effba54178b285bd80244ed0f4_4\n" +
			"ts_aeaa3cbfdae74ff09401e7025dc8e09e_4\n" +
			"b36ce4f09ca842b495c4d4dfbcdf8965_4\n" +
			"ts_bd139ef82b6c4950925c7518007e3693_4\n" +
			"ts_11f1272eb986489480c04379ce5aeffa_4\n" +
			"ts_dd413095dbba4949a14fffd063db67d6_4\n" +
			"ts_7a3f8e51229c45d398de796d31bd32e4_4\n" +
			"ts_b5c11a66b20b49929ab0dddc0939614c_4\n" +
			"ts_c9111ac37b8a4be381c1bd5d2c319a05_4\n" +
			"bd_e05542fdd91c4025a9493fe4b3360fdf_4\n" +
			"ts_294a5d39bcaa4c3e89ae762b16126b24_4\n" +
			"ts_abd12cf6432b486fa2e21a5e5bd7b01f_4\n" +
			"ts_6694309dcf4d4fd9bcce16be79d68f5b_4\n" +
			"bd_ed1675b25641409fa67da498c4f86bfb_4\n" +
			"ts_519f39507f844d0a95b239c6a6d80467_4\n" +
			"ts_92fbf648a3ed4c528fd784d0dd6eb2da_4\n" +
			"ts_256f6111e4b44eaf8e18606a220d22ac_4\n" +
			"bd_8fb165dc97d6440088412e11f87a6395_4\n" +
			"bd_f5ba6086dab24f8abf1005a3d9124337_4\n" +
			"ts_12474ec82d9340ba9bc209309f9eb9c7_4\n" +
			"ts_78eba3f09ac94ab882118a9e4a6de688_4\n" +
			"bd_b5afa6c686bb41d4b2b8dc7022f449d0_4\n" +
			"c2411ac2a1e945ab80d9fca8b24b8733_4\n" +
			"ts_becc14710d964b92b23e0c2798f0ea87_4\n" +
			"ts_36d4c309ce4e464e8d6f2c637e3bcc03_4\n" +
			"bd_78744fd47a2b44f6b859e58ba2984eb8_4\n" +
			"bd_5cddd171b7c04c45a386baa14046feba_4\n" +
			"ts_d0b1cfb5b1e94a14b1b0fc95bd383e40_4\n" +
			"ts_f5b2e8ef6290407da946247c85e9388f_4\n" +
			"ts_9c29b49f35ad404bb794b7c4db7193a0_4\n" +
			"ts_2155d73194ed4cdb8c007743f7e00688_4\n" +
			"ts_b528b391a74c46bca178828bb22a1286_4\n" +
			"ts_5b5981db65224a2d87846b53c08b8364_4\n" +
			"ts_b74cebedc420419da9c3699c04d4a2e8_4\n" +
			"ts_2c9b823a968b43bb96308ebba435f37d_4\n" +
			"ts_d8c3df165bc245a3bcbbf80d3c548cb5_4\n" +
			"ts_5b1ec38d2d6848b3ad04738b72d7935d_4\n" +
			"ts_79e0b6ae119a45218c428358a79cb5be_4\n" +
			"ts_d540ee29b6ab4559a9b532061d46d494_4\n" +
			"ts_28f0ac6a9e2349318d758975f97fe672_4\n" +
			"dced850485e241a38b023f856dae1e30_4\n" +
			"ts_d34c8f4e93994604ad15bd8cf8313305_4\n" +
			"ts_51cd61a8aa5d4360b48180b92101c1ef_4\n" +
			"ts_8f03eae5899c43ce97469d5b07fbf0cd_4\n" +
			"ts_f0ab75d097134d98b3b49ff379a4d2e0_4\n" +
			"ts_f7f64b4fea714e979cce367f1d54ded9_4\n" +
			"ts_382b689313664f649f794d96d900b261_4\n" +
			"ts_d5b4432e4aa34d68bfa6d1f21145453d_4\n" +
			"ts_01dffc4ea9af4969b8a5d6aef2f49e72_4\n" +
			"d019d12e9d544fe3b6420cc68c38842d_4\n" +
			"bd_37ef89eedd9842449d701bbe7bdaef0f_4\n" +
			"ts_4b2669d910b444df9b71da9efc7fa1e8_4\n" +
			"ts_49aefb41957f497d97dea9432654a3c8_4\n" +
			"bd_6928b403b7d64d26a057344cadba1725_4\n" +
			"bd_01a5422101824555a6f2199395d485e6_4\n" +
			"ts_db8785a60a404e87a89fdeb40c2e1769_4\n" +
			"ts_0a5999128c804afda549614a84d2b945_4\n" +
			"ts_fcd16de359b648b5880bdf06e348bd01_4\n" +
			"ts_db4e8a54a8224b9386eee7d8abf0797a_4\n" +
			"ts_1b775ec5233645d7900d38cbcac2e7ff_4\n" +
			"ts_9064b7928e3f42bab7bcc9b3c9297aaa_4\n" +
			"ts_dc91b213e3aa480a89987910532c1f3a_4\n" +
			"ts_1c7a59805eef4026b05648f7002b9b26_4\n" +
			"ts_47a35525dd1943bf86043ce1694c6c33_4\n" +
			"bd_d622be86c8ca45518bc1c4ea0bcd54ab_4\n" +
			"ca200a68cb434dd3a2d876e4df4a870d_4\n" +
			"ts_5d9b3f2e7159466396805363ad15a9bc_4\n" +
			"ts_2e77b8a1fc2f423fa4153e822e3806a3_4\n" +
			"5c7d859a48ca4d46bbdba70a16f11b3e_4\n" +
			"407bf675d811428dbb8428843eca4d76_4\n" +
			"cb77d5e78a454671a97624d76899408d_4\n" +
			"bd_7330216979004fee9f2b7b1a9be5b65c_4\n" +
			"bd_f8c5261fc7a84d3ab1c93c85c5d1d3bc_4\n" +
			"bd_b097af5967dc47f0a078465e79a5deb7_4\n" +
			"deb743ec4ce24a1d9e4d34ee0b5b1640_4\n" +
			"e83952e288f6488e9b319d0996a056e7_4\n" +
			"bd_8d9b4cfd2275400c813ee5a0773d43f7_4\n" +
			"ts_8daa1e1e268a4a9685db573c77d0e651_4\n" +
			"f743b55c11e54e15b905042dba2ea38e_4\n" +
			"ts_8bec2f937e774bb190ec6522fcf92b78_4\n" +
			"ts_a2fba34721e94defa04bad7aef605f20_4\n" +
			"ts_62db720e46064c0290c2b959e2a07630_4\n" +
			"ts_943081bcc69141c59e0680f15f96022b_4\n" +
			"295abcfe4dfe4e3cbd0cf0f3daea6629_4\n" +
			"bd_fce27c3f83d945669e22c23d4feeea21_4\n" +
			"ts_7b41fe6ca0e2401285f56863643fe8e4_4\n" +
			"ts_1b2f80155f484ffeb9830580a9062d62_4\n" +
			"869111d656c64afc8f93427c9a15798e_4\n" +
			"aa722e999ce444cfb3335eee9994633b_4\n" +
			"fda6f73090ee4571b7672805b475306b_4\n" +
			"7c8579fd257544bca9df9d543c13f782_4\n" +
			"bd_98b102d0804d4daf8281da35921e5231_4\n" +
			"5049060b0c7245cca1b10968259767bc_4\n" +
			"a43180c6eaca4ce49a5f7edb13ab2f6a_4\n" +
			"e71de66a322b4d7bb902c50b5264c3b0_4\n" +
			"bd_d4e7da4c113d4ada9d37a43bb30b0e72_4\n" +
			"ts_635e7023f3db4d55994214493f07fe70_4\n" +
			"fe456f6effdf4585a4b489d20af503e3_4\n" +
			"b32f161171944d27936796aba5c53770_4\n" +
			"458956528b144c2cb4b4aa68d4618f21_4";

}
