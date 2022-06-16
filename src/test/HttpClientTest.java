package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;  
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;  
import java.security.KeyManagementException;  
import java.security.KeyStore;  
import java.security.KeyStoreException;  
import java.security.NoSuchAlgorithmException;  
import java.security.cert.CertificateException;  
import java.util.ArrayList;  
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;  
import org.apache.http.ParseException;  
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;  
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;  
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;  
import org.apache.http.entity.mime.content.FileBody;  
import org.apache.http.entity.mime.content.StringBody;  
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import util.http.HttpClientUtils;

//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.helloworld.template.entity.Lesson;
//import com.sun.tools.internal.jxc.ap.Const;
  
public class HttpClientTest {  
  
    /** 
     * HttpClient连接SSL 
     */ 
    @Test
    public void ssl() {  
        CloseableHttpClient httpclient = null;  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            FileInputStream instream = new FileInputStream(new File("d:\\tomcat.keystore"));  
            try {  
                // 加载keyStore d:\\tomcat.keystore    
                trustStore.load(instream, "123456".toCharArray());  
            } catch (CertificateException e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    instream.close();  
                } catch (Exception ignore) {  
                }  
            }  
            // 相信自己的CA和所有自签名的证书  
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();  
            // 只允许使用TLSv1协议  
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,  
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);  
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();  
            // 创建http请求(get方式)  
            HttpGet httpget = new HttpGet("https://localhost:8443/myDemo/Ajax/serivceJ.action");  
            System.out.println("executing request" + httpget.getRequestLine());  
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {  
                HttpEntity entity = response.getEntity();  
                System.out.println("----------------------------------------");  
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                    System.out.println("Response content length: " + entity.getContentLength());  
                    System.out.println(EntityUtils.toString(entity));  
                    EntityUtils.consume(entity);  
                }  
            } finally {  
                response.close();  
            }  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (KeyManagementException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (KeyStoreException e) {  
            e.printStackTrace();  
        } finally {  
            if (httpclient != null) {  
                try {  
                    httpclient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
  
    /** 
     * post方式提交表单（模拟用户登录请求） 
     */ 
    @Test
    public void postForm() {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost("http://220.191.224.121:9082/yypt/search/book.xhtml");
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("sjrsfzh", "330621197101012151"));
        formparams.add(new BasicNameValuePair("sjrxm", "陶国强"));
        formparams.add(new BasicNameValuePair("sjrlxdh", "18817878571"));
        formparams.add(new BasicNameValuePair("sjrybkh", "B3052158X"));
        formparams.add(new BasicNameValuePair("qydm", "330602"));
        formparams.add(new BasicNameValuePair("yddm", "zytzd"));
        formparams.add(new BasicNameValuePair("xxdz", "陶堰镇104国道北面阿祥不锈钢门业"));
        formparams.add(new BasicNameValuePair("random", "7194"));
        UrlEncodedFormEntity uefEntity;
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                    System.out.println("--------------------------------------");  
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
                    System.out.println("--------------------------------------");  
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果 
     */  
    @Test
    public void post() {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost("http://tongjilab.cn:7788/login/");  
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("type", "house"));  
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                    System.out.println("--------------------------------------");  
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
                    System.out.println("--------------------------------------");  
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
  
    /** 
     * 发送 get请求 
     */ 
    @Test
    public void get() {  
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet("http://tongjilab.cn:7788/lesson/?name=嵌入式系统");
//            HttpGet httpget = new HttpGet("http://localhost:8080/education-system/service/sayHello.do?name=天龙");
//            HttpGet httpget = new HttpGet("http://www.baidu.com");
            System.out.println("executing request " + httpget.getURI());  
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();  
                System.out.println("--------------------------------------");  
                // 打印响应状态    
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                    // 打印响应内容长度    
                    System.out.println("Response content length: " + entity.getContentLength());  
                    // 打印响应内容    
                    String result = EntityUtils.toString(entity, Consts.UTF_8);
                    System.out.println("Response content: " + result);
//                    ObjectMapper mapper = new ObjectMapper();
//                    JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Lesson.class);
//                    List<Lesson> lessons = mapper.readValue(result, javaType);
//                    System.out.println(lessons);
                }  
                System.out.println("------------------------------------");  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    
    
  
    /** 
     * 上传文件 
     */  
    @Test
    public void upload() {  
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        try {  
            HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceFile.action");  
  
            FileBody bin = new FileBody(new File("F:\\image\\sendpix0.jpg"));  
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);  
  
            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();  
  
            httppost.setEntity(reqEntity);  
  
            System.out.println("executing request " + httppost.getRequestLine());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                System.out.println("----------------------------------------");  
                System.out.println(response.getStatusLine());  
                HttpEntity resEntity = response.getEntity();  
                if (resEntity != null) {  
                    System.out.println("Response content length: " + resEntity.getContentLength());  
                }  
                EntityUtils.consume(resEntity);  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    
    @Test
    public void postHttps(){
    	// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost("https://e6643e30271042f1aac8619116111640-vp0.us.blockchain.ibm.com:5003/registrar");  
        try {  
        	// 创建参数    
        	String jsonStr = "{\"enrollId\": \"admin\",\"enrollSecret\": \"56d4a82f3d\"}";
        	StringEntity stringEntity = new StringEntity(jsonStr, ContentType.create("application/json", "UTF-8"));
            httppost.setEntity(stringEntity); 
        	
            System.out.println("executing request: " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                    System.out.println("--------------------------------------");  
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
                    System.out.println("--------------------------------------");  
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
    }

    @Test
    public void testConnPoolLeak() {
        System.setProperty("org.apache.commons.logging.LogFactory", "org.apache.commons.logging.impl.LogFactoryImpl");
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.simplelog.defaultlog", "error");

        ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager();
        connManager.setDefaultMaxPerRoute(20);
        connManager.setMaxTotal(20);

        HttpParams httpClientParams = new BasicHttpParams();
        // 同时也会设置从连接池中获取连接的时间
        HttpConnectionParams.setConnectionTimeout(httpClientParams, 10000);
        HttpConnectionParams.setSoTimeout(httpClientParams, 10000);
        HttpConnectionParams.setStaleCheckingEnabled(httpClientParams, true);
        HttpConnectionParams.setTcpNoDelay(httpClientParams, true);

        DefaultHttpClient httpClient = new DefaultHttpClient(connManager, httpClientParams);

//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(30000);
//                } catch (InterruptedException e) {
//                }
//                System.out.println("------clean");
//                connManager.closeIdleConnections(1, TimeUnit.SECONDS);
//            }
//        }).start();

        HttpRequestBase httpRequest = new HttpGet("http://quickapi.wending.yuedu.163.com/test/2");
//        HttpRequestBase httpRequest = new HttpGet("https://easyreadfs.nosdn.127.net/wwb.bb7d2c29646c49d6bda5cdd383ad51f6.jpg");

        for (int i = 0; i < 30; i++) {
            try {
                HttpResponse response = httpClient.execute(httpRequest);
                InputStream content = response.getEntity().getContent();
//                System.out.println(IOUtils.toString(content, "UTF-8"));
//                content.close();
//                BufferedImage image = ImageIO.read(content);
                System.out.println("call-" + i + ": " + response.getStatusLine().getStatusCode());
//                System.out.println("call-" + i + ": " + response.getStatusLine().getStatusCode() + " size " + image.getWidth() + ":" + image.getHeight());

//            String result = EntityUtils.toString(response.getEntity());
//            System.out.println(result);
            } catch (Exception e) {
                System.out.println("error, call-" + i + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

    }
    
    
}