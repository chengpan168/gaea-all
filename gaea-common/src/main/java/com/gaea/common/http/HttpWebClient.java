package com.gaea.common.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Fe on 14-9-15.
 */
public class HttpWebClient {

    public static final Logger logger = LoggerFactory.getLogger(HttpWebClient.class);

    public static final int DEFAULT_MAX_PER_ROUTE = 50; // 每个路由的默认最大连接数
    public static final int DEFAULT_MAX_TOTAL = 100; // 连接池里的最大连接数


    public static final String DEFAULT_ENCODING = "UTF-8";


    public static HttpClient httpClient;

    static {
        PoolingClientConnectionManager poolingClientConnectionManager = new PoolingClientConnectionManager();
        poolingClientConnectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        poolingClientConnectionManager.setMaxTotal(DEFAULT_MAX_TOTAL);
        httpClient = new DefaultHttpClient(poolingClientConnectionManager);
    }
    /**
     * 带参数的Http-Post请求
     */
    public static String post( String remoteUrl, Map<String, String> params) {

        if (StringUtils.isEmpty(remoteUrl)) {
            throw new RuntimeException("post remoteUrl not null !");
        }
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(remoteUrl);
            List<NameValuePair> nameValuePairList = null;
            if (params != null && params.size() > 0) {
                nameValuePairList = new ArrayList<NameValuePair>();
                for (Iterator<String> itr = params.keySet().iterator(); itr.hasNext();) {
                    String key = itr.next();
                    String value = params.get(key);
                    nameValuePairList.add(new BasicNameValuePair(key,value));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList,DEFAULT_ENCODING));
            }

            HttpClient httpClient = getHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            result =  EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            logger.error("httpclient post error , e {}", e);
            throw new RuntimeException("connect server error!");
        }
        return result;
    }

    /**
     * 无参数的Http-Post请求
     */
    public static String post( String remoteUrl ) {
          return post(remoteUrl,null);
    }

    /**
     * 无参数或remoteUrl已带参数的Http-Post请求
     */
    public static String get(String url) {
        return get(url,null);
    }

    public static String get(String url,Map<String,String> headers) {
        String result = "";
        HttpGet httpGet = new HttpGet(url);

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String,String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(),entry.getValue());
            }
        }

        try {
            HttpResponse response = getHttpClient().execute(httpGet);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("execute get error , url : {}, exception : {}",url,e);
        } finally {
            httpGet.releaseConnection();
        }
        return result;
    }


    public static String jsonGet(String url) {
        String result = "";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("X-Requested-With","XMLHttpRequest");
        try {
            HttpResponse response = getHttpClient().execute(httpGet);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("execute get error , url : {}, exception : {}",url,e);
        } finally {
            httpGet.releaseConnection();
        }
        return result;
    }

    public static InputStream downLoadFile(String url) {
        InputStream input = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = getHttpClient().execute(httpGet);
            input = response.getEntity().getContent();
        } catch (IOException e) {
            logger.error("execute get error , url : {}, exception : {}",url,e);
        } finally {
            httpGet.releaseConnection();
        }
        return input;
    }

    public static HttpClient  getHttpClient() {
           return httpClient;
    }



}
