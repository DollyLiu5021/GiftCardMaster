package com.example.giftcardmaster.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(32);
        connManager.setDefaultMaxPerRoute(8);
        HttpUtil.httpClient = HttpClients.custom().setConnectionManager(connManager).build();
    }

    public static String sendGet(String url){
        logger.info("Send Get Request: {}", url);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            logger.info("Code: {}", response.getStatusLine().getStatusCode());
            return EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static String sendPostWithHeader(String url, Map<String, String> paramsMap, Map<String, String> headersMap) {
        logger.info("Send Post Request: {}", url);
        HttpPost httpPost = new HttpPost(url);

        // Set headers
        for (Map.Entry<String, String> headerEntry : headersMap.entrySet()) {
            httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
        }

        // Set parameters
        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        for (Map.Entry<String, String> paramEntry : paramsMap.entrySet()) {
            postParameters.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            logger.info("code: {}", response.getStatusLine().getStatusCode());
            return EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "error";
    }
}
