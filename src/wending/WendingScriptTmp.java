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
public class WendingScriptTmp {

	private static boolean apiInsert(String bookId) throws Exception {
		CloseableHttpClient httpClient = HttpClientUtils.getDefaultHttpClient();
		Map<String, String> header = new HashMap<>();
		header.put("Cookie", "NOVEL_FINANCE_BACKEND_U=b964f85e-7d6c-48af-8c90-884eb3ae4cdb-1632306699950; _us.c.v.online=YR9wEQA5FxoqO0ItZC4JDFpSBkBMQEFfbA5UclVfXxdIcVwaVgJPelVPTQFASw0208fd79");
		Map<String, String> params = new HashMap<>();
		params.put("bookId", bookId);
		params.put("newPrice", "0.99");
        params.put("csrf_token", "50704df8b8419bb6b11e2cfc91919d78");
		HttpClientUtils.Response response = HttpClientUtils.post("https://wending.hz.netease.com/book/mirror/setAudioArticle.json", header, params, null, httpClient);
		if (Objects.equals(response.getStatusCode(), 200)) {
			JSONObject jsonObject = JSONObject.parseObject(response.getResponseBody());
			Integer code = jsonObject.getInteger("code");
			if (Objects.equals(code, 0)) {
				return true;
			}
		}
		System.out.println(response);
		return false;
	}

	@Test
	public void read() throws Exception {
		String[] split = bookIds.split("\n");
		int count = 0;
		for (String s : split) {
			System.out.println(s);
			if (!apiInsert(s)) {
				System.out.println("error: " + s);
			} else {
				count++;
				System.out.println("succ: " + s);
			}
		}
		System.out.println("succ:" + count);
	}

	private static String bookIds =
			"audio_67ab575597134ed78576297573422a32_4\n" +
			"audio_97e3488aa8a141009070bcfbb5ab5e5b_4\n" +
			"audio_7554fbb6936f4b2790b57a4c40f7a23d_4\n" +
			"audio_e2b36fc32d604a5a9fb791729fdd4c4e_4\n" +
			"audio_894b54a876254c52a4ff701fe6886ed4_4\n" +
			"audio_cc6cd03c03ba4c378f4f13b1b5087911_4\n" +
			"audio_9111328777924c3ca85dc9f6df07cfec_4\n" +
			"audio_1ef0a3449e60480984bbef103b9fae01_4\n" +
			"audio_6141800a9dae4baca5e3c9f70a0ccbd0_4\n" +
			"audio_ee49345a3fb143618f60add2b4fbc9b1_4\n" +
			"audio_3f201b69dee84689a0c4eb0c8a2389df_4\n" +
			"audio_29cf5de0f55a4bd8a005ce998b8ca201_4\n" +
			"audio_96006d4949f446968fc49193c2ad72f0_4\n" +
			"audio_19b6c0dadec44e97a2f51ac01a1778ac_4\n" +
			"audio_740a1f65d03046a5ab97eadb0541021a_4\n" +
			"audio_d654d62ce5634664b4eb9f8a75750701_4\n" +
			"audio_02a59a4b3c4b41c7b000d0968391a47a_4\n" +
			"audio_bf4144c70a0041f8aeca23c8013b10da_4\n" +
			"audio_8a651e99f03645d58c33143453e87cae_4\n" +
			"audio_880a6fb6eff745a1bbcd29a0ae9ee977_4\n" +
			"audio_aea5c109062f4728a33fafa16d095e50_4\n" +
			"audio_68d2bb30251b45738898a1209a18c87e_4\n" +
			"audio_a7423c6fe0f5472aa7142ae1b3049a88_4\n" +
			"audio_1187edb31acc45e38bc073c2fe7c244b_4\n" +
			"audio_d33564782cf143159fee892fde125922_4\n" +
			"audio_898319f4c9ee4813af67bb08d2892afd_4\n" +
			"audio_12a70f9dfcd64294ae5ca00afab573b0_4\n" +
			"audio_c0a1c893612c4c408dfe7af722185500_4\n" +
			"audio_b8f8fc664a8c4ae58d58f53360546e56_4\n" +
			"audio_4c20edd5a6094067ac179e67eec0694f_4\n" +
			"audio_bc15967f2f9d4489991fbf8dc2b4563e_4\n" +
			"audio_b05a87dbf33240709077df235b511383_4\n" +
			"audio_62f73e65fda64ab5830fdccdf60f1fab_4\n" +
			"audio_177db7f747b14c40b10b51d903a2bd10_4\n" +
			"audio_17b5e6e08ab1489e8a776fb5480360b7_4\n" +
			"audio_73876cc510a046e99a112e403a7ec62f_4\n" +
			"audio_05974c722ddd4e66a07677d15bf21a1c_4\n" +
			"audio_82589c3baa55430087cb83c3190c7518_4\n" +
			"audio_46b6dcdc9b4b4078a92b01b644bebf6a_4\n" +
			"audio_a0fb0789c5a644048649566181f230a9_4\n" +
			"audio_06c5980ce8f74635b226d46465e1cc56_4\n" +
			"audio_ea87c2652e5a4064850e10cd71829b42_4\n" +
			"audio_831910880fdd457d9c978c32e7b536df_4\n" +
			"audio_26124f613ddf42638ab64a9188d6ca75_4\n" +
			"audio_87f6acd7275242ce8b3fccc2e5bd422f_4\n" +
			"audio_df5b8dd06d3f442d880ac6ce297383e1_4\n" +
			"audio_e27c61a54ffd439793eff435e8c12c75_4\n" +
			"audio_060931f3bf43460193f0c515ed2bed23_4\n" +
			"audio_3e873e3055b54604b572f290174e0fc6_4\n" +
			"audio_131d281206e44597b71a212092f5ea6e_4\n" +
			"audio_f4e05aba13344337b9570bfdd71e9413_4\n" +
			"audio_e711d8f3236648dca4ffee9aea15b1af_4\n" +
			"audio_4487ba396c814934b6a8d0c3fb7d4060_4\n" +
			"audio_009dad223c584226a0869d4d6fddd9db_4\n" +
			"audio_bf32668f46b24d3d80dc92562ccabfbd_4\n" +
			"audio_615ee3673ff14f32bf54a398fc31c5c3_4\n" +
			"audio_2a63943c3eb445779b8ac8d05f24389b_4\n" +
			"audio_0ef7b1c45ff04de285867c23c8c01b0c_4\n" +
			"audio_294aac0ce7ca4479ad6fbf2ece7ca372_4\n" +
			"audio_0bfae61b6d2f4758b516788d9df26199_4\n" +
			"audio_d8e2327cff3d4a92b677f76b1f97ddee_4\n" +
			"audio_c12483da2c944f99ad4047ad4dec9ca3_4\n" +
			"audio_bd7bfc621f0241ee87a0f47cb2cf8fd1_4\n" +
			"audio_d492273c7864428bac7674df1a245d75_4\n" +
			"audio_d6fe273cbd534b5a8dd5d7f0d570aa35_4\n" +
			"audio_8a016ae399c74bab91debcec139018d3_4\n" +
			"audio_cf559bbbda5245a8a76c4fc65b9b52ae_4\n" +
			"audio_949561c7a7fa49c5a736360f4c1747f8_4\n" +
			"audio_41bf72a6661d4bb18e8972a2a965ea20_4\n" +
			"audio_c8a820cb42c94645b650f3b0aa163cef_4\n" +
			"audio_77af5d29b08c464186ad0487fbc152e0_4\n" +
			"audio_033c6f0db0104372b43cf3146276733f_4\n" +
			"audio_9fec82e10d6d46f085543711767e43b7_4\n" +
			"audio_2c0acee8f3e443aebabb1fe8cf946d59_4\n" +
			"audio_9a0fd47c125b4caf8ba5d2d8515578b9_4\n" +
			"audio_80b71c1df329443fbd5e4199e79fc5e4_4\n" +
			"audio_b60f708b74dc4e848c7ae415bf797649_4\n" +
			"audio_0aeb518bd10a4c71b9de57130a15633a_4\n" +
			"audio_a2bfcc484bb2418da4972f64594911cd_4\n" +
			"audio_7a0768c7ee324821b763a9319214477d_4\n" +
			"audio_023aaeecaa514e2091bad33dfb00f982_4\n" +
			"audio_2a5bf96bbeeb4e0c89d9e1b92498256c_4\n" +
			"audio_3f86520db90d417abed2c695280e5bda_4\n" +
			"audio_675b91f03bee4ba290e9b8259867abb8_4\n" +
			"audio_924f048fdc0b4ab99d3580bb46b206a4_4\n" +
			"audio_a4834ce943b64e338ce2351dc3443e19_4\n" +
			"audio_0e4e31c18c7041e5ae2d70a2aa887ca5_4\n" +
			"audio_ab881c18d7a44de58be6cddea41a0f5c_4\n" +
			"audio_6cc221a0474348d8bd2ebe70516a59be_4\n" +
			"audio_d16b8e9179414cd09271ca5e4152c8f0_4\n" +
			"audio_5075edd9fe5348b3a95924585502b309_4\n" +
			"audio_23f5238951e04fcea57e31589e995b15_4\n" +
			"audio_a527d6badbdc4af1907e0504c180f90c_4\n" +
			"audio_859cc97d2a764ca48b1b5ad8c6394c34_4\n" +
			"audio_166c7c372c1f48888f0fd0f3fe45c59a_4\n" +
			"audio_f544c99cf12d41eab4e3309de8956b24_4\n" +
			"audio_6bfecde2c0b645aa95251cbb3ea1dea0_4\n" +
			"audio_5a3a62e16d914b6584b0e4a8793b9786_4\n" +
			"audio_807a1d0ecd054bf7bef1a52ae23f4345_4\n" +
			"audio_8031cbac4d8245f6b3ff8bfa13a7b361_4\n" +
			"audio_ca9d0b6bdc244c62b6a1f2c93ef58bcf_4\n" +
			"audio_a73293d834b44f48bff0486b2097d4d7_4\n" +
			"audio_e6f50ce4cfed4db0b970dec2772e1697_4\n" +
			"audio_3047d15e69ac4e3cad6513c9ae5080ff_4\n" +
			"audio_2efd65f7d2e24e8597e4d7f1535666ed_4\n" +
			"audio_a1a568c14a1d4675919de4ded0207767_4\n" +
			"audio_55680490688d4d94b2b933044a11cd64_4";


}
