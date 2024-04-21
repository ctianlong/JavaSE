package test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author ctl
 * @createTime 2023/10/18 21:22
 * @description
 */
public class SignTest {

    @Test
    public void test() throws Exception {
//        String privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZSHNcFfthd/bVYexEJWOBVEjjDcXjfr1fYevuraNFfMmLPKV836BbvCiUSWHzJYEpkJ934e/j28NBEcEbPDLiGlLTd6AVwR22TkUwpLr41oQprz0HKFwhVPZ0HQCGIv0pVMA53TFSitIqiqbNLmgm5yzSNqNy1t/0X/RfqEtA6Eoxw9u/Sx57i+pBFuLlZYanlm57+b7t1khg9JHvF0ulo7DScyJ4qgrD7oQf0RIQB0rqCFIeYuYO1cfvnxb9x4DPodEyVoAM4i9YdFop9ZHt73W/icuLku/P8/G1+arzB5b7S1S3ky5/KdS8AEA9Ww5czZcdf9Jgm2S6RymjFGjzAgMBAAECggEBAIryGNgdePyWcSJmHHR9a+CdFWD0aDBa/7CJpAN8VKc1gcB8Xgp+7+6X9jTM/EQa+CVEWrmiDgF/gVPnkyNsAzff4rqcEnoFzzglZSS9/lp4od7jYa+uTy1LxgflDkeJSfEASStqrT4EZpR3kNInQfQZ1BBNxQXhb6smm+9mL6kKQJjAqBgEqtUAmNv0GnH89ZPPgZuIZeeL4cb4BhMEoa5MBnI+HDf07cN1nECQXRJlHU/iyhAPfP7RpO8O9KGDEDE36qebu0Cu4yUjWANXiqECFv93sQzONotkl3VPealvXM+jzGT7YdgHo/t3QKE8flMBo/XUzGTqi8j5AOXiaBkCgYEAylKVtjQMYgg4qMwd9Je+KZ9qL6QVHCsB2NPUt8N99oj70efsG4aGaEAadr8meNhIJ5lpoK+FXqSBIbbDS/xeOVI3XoMx/EdKLw/ZNi87G/EHYK9z7Fr3W7q8DFXe2hZ/ojFXC/aaBanjVVBK/6RfPzXfnx+vGX/t1FhcLC+yQD8CgYEAwfMtrXfH+3dW77dxXT/CTFJVs/o1K2qbepnQ6A33KMHPLBtPZZ6z5rzIO7OMSNItOTXTEoRXHmOKc5FtXGtbCvGSRByb6FgDWG3p2Bp2sZLuJBzXmLbSnEbHTNHM6uTgxNgWAh8pYpjPY8xF7BqYz2rGT47OPBmctRzDjnzjak0CgYAqkM1mk/S2+zvQZ4E14GblouBYPZEjZ/jvgUGTl9F8eL1iIAUQlXDZpgLrULPrYLVtf101rTfF/Z4dVbIo3mOEc8OqYre1d9onpJHyUGWDL2Z59O/SniDEb7j4b2h/QZSArxi9L5if8GofnNDqj85qIg92Dthr6PpEXoKl2TMLSQKBgFzQBHHYukiqSV4ZyRQ4qMBhPkYMXFlUgObgqMoDtN06MewHfa1BjxHCEYgQWfeXLLEOAt3/mrkeJWk8lLr/XOgVxkr17d34EFHG93rE3zwG9hMuAjZAdvT2IfWvCIL32GAakB2fz+ww+D3nySY9bBcGH7R+wE6eaxF4nFSZizKZAoGBAJzuaWCnVK0djfgvUsjmGUtyDvgyREcpAsXvES1pB2NLVeEUxm0uRtj6k4DhCv3rJfUwfMr0+sa9NUnXuaSRVqLYvAD8bNPKXwn7ymzQ7WioCqmZuUhLnQRppkjhfQGKLH0MnMw9Xh9FwJ9kzGNEUnTEhaaHsoaHMlLlRET32gyG";
//        String enc = getSignature(privateKeyStr, "POST", "/abc", 1680835692, "gjjRNfQlzoDIJtVDOfUe", "{\"eventTime\":1677653869000,\"status\":102}");
//        System.out.println(enc);
//        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmUhzXBX7YXf21WHsRCVjgVRI4w3F4369X2Hr7q2jRXzJizylfN+gW7wolElh8yWBKZCfd+Hv49vDQRHBGzwy4hpS03egFcEdtk5FMKS6+NaEKa89ByhcIVT2dB0AhiL9KVTAOd0xUorSKoqmzS5oJucs0jajctbf9F/0X6hLQOhKMcPbv0see4vqQRbi5WWGp5Zue/m+7dZIYPSR7xdLpaOw0nMieKoKw+6EH9ESEAdK6ghSHmLmDtXH758W/ceAz6HRMlaADOIvWHRaKfWR7e91v4nLi5Lvz/Pxtfmq8weW+0tUt5MufynUvABAPVsOXM2XHX/SYJtkukcpoxRo8wIDAQAB";
//        boolean raw = verify("", publicKey, "RFQ65hHlo4xyZ6EC31LZC0SzsyN0nd2Fb2wAiISvY1mkiC6G8gn2QZwLGq7qgjenRGl/Z8OrTtkBHWb9GOazkJFkHrPeRqogqnwZ+kSOxGvtou8FPN669E1wwb+BShN4pIUgPFzaukR9/rCRBsbLoq9RPVA2sbf3iKoHGa81zhXjQSuFbF1CyiWkL5qqniNTM/BSfwfLZfPW8nBanRl3U+mQaymbj0DCF0ZdWhFz1FnZPAfEpx8YEwFNZWtxzz4p3WJ1swnUocJC4LXoDazo6DhEPDuoZXOXrB1SqzL1wRqA4p8uj3Z8Seki/PMGWiGpGWPMv3tJyvWmzMOuVJtEjg==", 1680835692, "");
//        System.out.println(raw);

        String privateKeyFile = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC8sV2zQXzzSe+0\n" +
                "W0suxs8UnU60uda3RxXXyLLTjt/tta0IR9dIxeLUCK/UOfYSPgS5l4VN95oK9Rj1\n" +
                "RxZaovkF5ECpzvjs5eUHm/y/drmtj1FfEozhRLawPlX0rUxMh+xOdjzS4w/5IxbK\n" +
                "wRma49qlQdORBJ9VyIIwV1BfD4hJVoXGVbY3TwRIQ/hu3bUIJhDueFCypO+OdqyZ\n" +
                "OfhiTZK2Ony5fZvgBxqbkyFQblrB/5t4JShLPORAud2BtRYVpRznzOiy0mYmwnfv\n" +
                "dGqWQeikk/YRDilJ/rQO3+7RCASw3lCaPNIwOWSjaPiWmH432+R443usQAdmRQ3n\n" +
                "OjPW+7OFAgMBAAECggEAVYyQr+HkFaWS4RF3D7khT/ZWJcgP2j6HX7LlqNchHXP+\n" +
                "BEAWEi9tvLZHiY/RycmGeOKClKvKkmc3LpVRRDyZbJtk2dg1sao1YXLOb7wTSOYu\n" +
                "fU06xDNUJKnObMVZT2VctOIUh4RczJalEb3krv3ZNo2WrywKVWk35VTg4oh3QNAK\n" +
                "CkM71KjkXD/vRD74Yw7vqEicjNKqgDxQn3R2VwC2bCHHjAfOVvH+ioTX+xxpmhoz\n" +
                "Z9Owsv8FPnWj41wTGo4L7GN+FE41xubcQ+HJX9EEmJqAZjQuXRLu5w6kXYtvF8fU\n" +
                "Z/SuNJ4QHLapNy4fzpZPYMiWxwxZjIKgfogfkKarAQKBgQD1b+RElK3Ct5Zdz5zU\n" +
                "LUGD6JA85NYy7ByBQEmlqYo8hQuSk2xP/abetEOGH7vU36DdpxeEYjFPgDFu6MH4\n" +
                "lbNI5SXU32AmvTiOehNbBV4+3B4uPmvQ6WZxLfc/QLucjjZArzFEJbpJqvABIhy/\n" +
                "9qA/8AeZVpkJ3f33JtZKKqgKwQKBgQDE0EsexRP98oz0Vh7vd5WzsE6C87t16rnm\n" +
                "dTKdUlI75e52gnfw/XygmaDDKLumSJ+jvInQjSBKkPdh++bnGC8MMKGg1UvSGmor\n" +
                "iPQllSjK4SKAKNI6Cf7HxKCYF71qB2w5YQdQg2hhGtNUTL6cNTRR0pEnol2sefuN\n" +
                "8vw9ESetxQKBgQDlyGzw3DXWACVyDMaqSvh0c85ehGwZ3r5NMBADXjhCstkD0Kvu\n" +
                "M+7/v8scDie/g+LKwf4dfgTmhoFBcf7wKc9hZs/FfaA6hJsnoRjv9ZWnylMjPmOY\n" +
                "gnvbaIp7mLJNP0OBycOZJq1C/RuYJATAoDilw6aJo+7BsylrM9waMdeDwQKBgQDE\n" +
                "aUZ+d5ki9nr6oDORMZthTpKblxc+bziEp/MSE4RjqGxZMoz2IFLOh2JrQOw73wAJ\n" +
                "zG8QhLZ0E8X9sAFYIMda9/Fk61gWXiwUNEHKe9fQ1+qxUYuzRiRQQocuIYrzFGNJ\n" +
                "w+4jyHBTcAz0dP5IL8R935Dip+Op67ZCi/p0Cvm5LQKBgDAf8AijpG+HhGpYbe5X\n" +
                "A75pFU/4Hbys+nxe4bg62C3UQBx/Yxdw/mkQgyZnwG1knqLxJgeP9t4abYmCzmxY\n" +
                "J2BwweNzXK0dTb+tW7Ddw+tClJF/ijgwOsdHqVDQhnnrVRgUmdY5/4KCcL1BQbXv\n" +
                "rp0pITH06rNXkUKib6lkzAjF\n" +
                "-----END PRIVATE KEY-----\n";
        String publicKeyFile = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvLFds0F880nvtFtLLsbP\n" +
                "FJ1OtLnWt0cV18iy047f7bWtCEfXSMXi1Aiv1Dn2Ej4EuZeFTfeaCvUY9UcWWqL5\n" +
                "BeRAqc747OXlB5v8v3a5rY9RXxKM4US2sD5V9K1MTIfsTnY80uMP+SMWysEZmuPa\n" +
                "pUHTkQSfVciCMFdQXw+ISVaFxlW2N08ESEP4bt21CCYQ7nhQsqTvjnasmTn4Yk2S\n" +
                "tjp8uX2b4Acam5MhUG5awf+beCUoSzzkQLndgbUWFaUc58zostJmJsJ373RqlkHo\n" +
                "pJP2EQ4pSf60Dt/u0QgEsN5QmjzSMDlko2j4lph+N9vkeON7rEAHZkUN5zoz1vuz\n" +
                "hQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";

        String enc = getSignature(convertPrivateKeyToOneLine(privateKeyFile), "POST", "/abc", 1680835692, "gjjRNfQlzoDIJtVDOfUe", "{\"eventTime\":1677653869000,\"status\":102}");
        System.out.println(enc);
//        boolean raw = verify("", convertPublicKeyToOneLine(publicKeyFile), "AIBqA9M2ZW6V4wBC2Y5H+8Rp+K/w6+2JjkDT2ZiDW/7WwJ+AYaADeDrbxflayyFZmR0XA9/wpG+cPeAe121vi/p3QDfSHzURE1jMYoJC8KoSzYJHpIdbJKvAvg4qFjY3hlqcTYIij9ugWkjE6iP6PJq1KlKQA/1oLlvzQKOqGsWn28inH8bfT3gxd28DjRpcmz667CbQ/ZSSj4fernBRWBuHfsTpKIYj75Y4XYD3wKO8XRgIhvwd7DEAZpwwtIPlNeGKojPiO5zKQ3kqQV871srrk2AVgwIf9ouMu1x886XINminVilZFfMhmqY90gHROK/Oq0ycMfgqB0qRXP6KPA==", 1680835692, "");
//        System.out.println(raw);
    }

    public static String convertPrivateKeyToOneLine(String privateKey) {
        return StringUtils.trimToEmpty(privateKey)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\n", "")
                .replaceAll("\\s", "");
    }

    public static String convertPublicKeyToOneLine(String publicKey) {
        return StringUtils.trimToEmpty(publicKey)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\n", "")
                .replaceAll("\\s", "");
    }

    public static String getSignature(String privateKeyStr, String method, String uri, long timestamp, String nonce, String body) throws Exception {
        //method内容必须大写，如GET、POST，uri不包含域名，必须以'/'开头
        String rawStr = method + "\n" +
                uri + "\n" +
                timestamp + "\n" +
                nonce + "\n" +
                body + "\n";
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(string2PrivateKey(privateKeyStr));
        sign.update(rawStr.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    public static PrivateKey string2PrivateKey(String privateKeyStr) {
        PrivateKey prvKey = null;
        try {
            byte[] privateBytes = Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            prvKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return prvKey;
    }


    public static boolean verify(String httpBody, String publicKey, String signStr, long timestamp, String nonce) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(timestamp).append("\n");
        buffer.append(nonce).append("\n");
        buffer.append(httpBody).append("\n");
//        String message = buffer.toString();
        String message = "POST\n/abc\n1680835692\ngjjRNfQlzoDIJtVDOfUe\n{\"eventTime\":1677653869000,\"status\":102}\n";
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(string2PublicKey(publicKey));
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return sign.verify(Base64.getDecoder().decode(signStr.getBytes(StandardCharsets.UTF_8)));
    }

    public static PublicKey string2PublicKey(String publicKey) throws Exception{
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }


}
