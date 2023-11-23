package com.huawei.openEuler.config;

import com.huawei.openEuler.utils.ConstantUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * es数据库连接配置
 *
 * @author zhangshengjie
 * @since 2022-5-27
 */
@Configuration
public class EsConfig {
    protected final static Logger logger = LoggerFactory.getLogger(EsConfig.class);

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        logger.debug(ConstantUtils.LOGGER_CONNECT_TRY);
        RestHighLevelClient restClint = null;
        try {
            HttpHost host = new HttpHost(ConstantUtils.HOST_IP, ConstantUtils.HOST_PORT, ConstantUtils.HOST_SCHEME);
            RestClientBuilder builder = RestClient.builder(host);
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

            logger.info("User: {}", ConstantUtils.HOST_USER);
            logger.info("Password: {}", ConstantUtils.HOST_PASSWORD);

            credentialsProvider.setCredentials(
                    AuthScope.ANY, new UsernamePasswordCredentials(ConstantUtils.HOST_USER, ConstantUtils.HOST_PASSWORD));
            builder.setHttpClientConfigCallback(f -> {
                f.setDefaultCredentialsProvider(credentialsProvider);
                f.setKeepAliveStrategy((response, context) -> 1000 * 60);
                return f;
            });
            restClint = new RestHighLevelClient(builder);
            logger.debug(ConstantUtils.LOGGER_CONNECT_SUCCESS);
        } catch (Exception e) {
            logger.error(ConstantUtils.LOGGER_CONNECT_FAILED, e);
        }
        return restClint;
    }
}
