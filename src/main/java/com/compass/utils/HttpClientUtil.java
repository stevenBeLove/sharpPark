package com.compass.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	
	private static final Logger log = LoggerFactory
			.getLogger(HttpClientUtil.class);
	
	private static final int HTTP_SOCKET_TIME_OUT = 5000;
	
	private static final int HTTP_CONNECT_TIME_OUT = 5000;
	
	/**
	 * 
	 * @param url
	 * @param charset StandardCharsets.UTF_8
	 * @return
	 */
	public static String sendGetUrl(String url,Charset charset,String refererUrl){
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
		configureHttpClient(httpClientBuilder);  
		CloseableHttpClient httpClient = httpClientBuilder.build();
		//设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HTTP_SOCKET_TIME_OUT).setConnectTimeout(HTTP_CONNECT_TIME_OUT).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.addHeader("X-Requested-With", "XMLHttpRequest");
        //创建一个返回信息的对象  
        CloseableHttpResponse response = null;  
        try {  
            response = httpClient.execute(httpGet);
            try {  
            	log.info(response.getStatusLine().toString());  
                HttpEntity httpEntity = response.getEntity();  
                return EntityUtils.toString(httpEntity, charset);
            } catch (IOException e) {  
            	log.error("httpclient---io---error",e);
            }finally {  
                response.close();  
            }  
        } catch (IOException e) {  
        	log.error("httpclient---io---error",e);
        } finally {  
            try {  
                httpClient.close();  
            } catch (IOException e) {  
            	log.error("httpclient--close---error",e);
            }  
        }
		return null;  
    }
	
	/**
	 * 
	 * @param url
	 * @param postParam
	 * @param charset  StandardCharsets.UTF_8
	 * @return
	 */
	public static String sendPostUrl(String url,Map<String,Object> postParam,Charset charset,String refererUrl){
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
		configureHttpClient(httpClientBuilder);  
		CloseableHttpClient client = httpClientBuilder.build();
        try {
        	//设置请求和传输超时时间
        	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HTTP_SOCKET_TIME_OUT).setConnectTimeout(HTTP_CONNECT_TIME_OUT).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
            //设置HttpPost的传参数  
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if(postParam!=null&&!postParam.isEmpty()){
            	for (Object key : postParam.keySet()) {
            		params.add(new BasicNameValuePair((String)key, (String)postParam.get(key)));  
				}
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, charset));  
            CloseableHttpResponse response = client.execute(httpPost);  
            int statusCode = response.getStatusLine().getStatusCode();
            String responseStr = null;  
            HttpEntity httpEntity = response.getEntity();  
            if (httpEntity != null) {  
                responseStr = EntityUtils.toString(httpEntity, charset);  
            }
            log.info("respStr = " + responseStr);  
            //释放资源  
            EntityUtils.consume(httpEntity);
            if("302".equals(statusCode+"")){
            	 String locationUrl=response.getLastHeader("Location").getValue();
            	 responseStr = sendGetUrl(httpPost.getLastHeader("Referer").getValue()+locationUrl, Charset.forName("utf-8"), null);
            }
            return responseStr;
        } catch (IOException e) {  
            log.error("sendPostUrl---error",e);
        }
        return null;
	}
	
	public static void configureHttpClient(HttpClientBuilder clientBuilder) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			clientBuilder.setSSLContext(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
