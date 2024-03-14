package com.coussy.docker.microservice1;

import com.coussy.docker.microservice1.http.MicroService2HttpClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    OkHttpClient okHttpClient() {
        return new OkHttpClient(new OkHttpClient.Builder());
    }

    @Bean
    MicroService2HttpClient microService2HttpClient(OkHttpClient okHttpClient, @Value("${microservice2.url}") String url) {
        return new MicroService2HttpClient(okHttpClient, url);
    }

}
