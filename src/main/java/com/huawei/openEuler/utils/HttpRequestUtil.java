package com.huawei.openEuler.utils;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * http的get请求
 *
 * @author zhangshengjie
 * @since 2022/08/12 17:15
 */
public class HttpRequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    /**
     * 发出 https 请求
     *
     * @param url url
     * @return Response
     */
    public static Response getResponse(String url) {
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return response;
    }

    /**
     * post
     *
     * @param url url
     * @param body body
     * @return Response
     */
    public static Response postResponse(String url, String body) {
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), body))
                    .build();
            OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.MINUTES)
                    .callTimeout(60, TimeUnit.MINUTES).build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return response;
    }
}
