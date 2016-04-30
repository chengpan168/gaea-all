package com.gaea.client;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

/**
 * Created by chengpanwang on 4/15/16.
 */
public class HttpClient {

    private static final Logger              logger  = LoggerFactory.getLogger(HttpClient.class);

    private static final CloseableHttpClient httpClient;
    public static final String               CHARSET = "UTF-8";


    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static <T> T getObject(String url, Map<String, String> params, Class<T> clazz) {
        String json = doGet(url, params, CHARSET);
        if (StringUtils.isBlank(json)) {
            return null;
        }

        JSONObject res = JSON.parseObject(json);
        if (MapUtils.getIntValue(res, Constants.AJAX_STATUS) != 200) {
            return null;
        }

        return JSON.parseObject(MapUtils.getString(res, Constants.AJAX_DATA), clazz);
    }

    public static String get(String url, Map<String, String> params) {
        return doGet(url, params, CHARSET);
    }

    public static String post(String url, Map<String, String> params) {
        return doPost(url, params, CHARSET);
    }

    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        logger.info("处理get请求, url:{}, param:{}", url, params);
        String result = null;
        CloseableHttpResponse response = null;
        try {

            List<NameValuePair> pairs = buildHttpParam(params);
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));

            HttpGet httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }

        logger.info("get 请求结果 :{}", result);

        return result;
    }

    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return 页面内容
     */
    public static String doPost(String url, Map<String, String> params, String charset) {
        logger.info("处理post请求, url:{}, param:{}", url, params);

        String result = null;
        if (StringUtils.isBlank(url)) {
            return null;
        }
        CloseableHttpResponse response = null;
        try {

            List<NameValuePair> pairs = buildHttpParam(params);

            HttpPost httpPost = new HttpPost(url);
            if (CollectionUtils.isNotEmpty(pairs)) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
            }
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }

        logger.info("post请求结果 :{}", result);

        return result;
    }

    public static List<NameValuePair> buildHttpParam(Map<String, String> params) {
        List<NameValuePair> pairs = Collections.emptyList();

        if (MapUtils.isEmpty(params)) {
            return pairs;
        }

        pairs = Lists.newArrayListWithCapacity(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            if (value != null) {
                pairs.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }

        return pairs;
    }

}
