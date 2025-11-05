package com.ruoyi.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  // 添加日志导入

/**
 * HTTP请求工具类（Ruoyi框架标准实现）
 */

public class HttpUtils {
    // 添加日志对象定义
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    // 超时时间（毫秒）
    private static final int TIMEOUT = 30000;

    /**
     * 发送GET请求
     */
    public static String get(String url, Map<String, String> params) throws Exception {
        // 构建请求参数
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = uriBuilder.build();

        // 创建HttpClient和请求
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(uri);
            // 设置超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(TIMEOUT)
                    .setConnectionRequestTimeout(TIMEOUT)
                    .setSocketTimeout(TIMEOUT)
                    .build();
            httpGet.setConfig(requestConfig);

            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            }
        }
        return null;
    }

    /**
     * 发送POST请求（JSON格式参数）
     */
    public static String post(String url, String jsonStr, String contentType) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            // 设置超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(TIMEOUT)
                    .setConnectionRequestTimeout(TIMEOUT)
                    .setSocketTimeout(TIMEOUT)
                    .build();
            httpPost.setConfig(requestConfig);

            // 设置请求头和参数
            httpPost.setHeader("Content-Type", contentType);
            if (jsonStr != null && !jsonStr.isEmpty()) {
                StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
            }

            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    return EntityUtils.toString(responseEntity, "UTF-8");
                }
            }
        }
        return null;
    }

    /**
     * 发送POST请求（表单格式参数）
     */
    public static String postForm(String url, Map<String, String> params) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            // 设置超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(TIMEOUT)
                    .setConnectionRequestTimeout(TIMEOUT)
                    .setSocketTimeout(TIMEOUT)
                    .build();
            httpPost.setConfig(requestConfig);

            // 构建表单参数
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }

            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            }
        }
        return null;
    }

    public static String sendPostWithHeaders(String url, String jsonBody, Map<String, String> headers, int timeout) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL apiUrl = new URL(url);
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // 设置超时时间
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            // 设置请求头
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 读取响应
            try (InputStream inputStream = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}