package com.coussy.docker.microservice1.http;

import com.coussy.docker.microservice1.exception.DependencyError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MicroService2HttpClient {

    private final OkHttpClient okHttpClient;
    private final String url;

    public MicroService2HttpClient(OkHttpClient okHttpClient, String url) {
        this.okHttpClient = okHttpClient;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    final ObjectMapper OBJECT_MAPPER =
            JsonMapper.builder().addModule(new JavaTimeModule()).build();

    public String callMicroService2() {

        HttpUrl httpUrl = HttpUrl.parse("%s/test".formatted(url));
        Request request = new Request.Builder().get().url(httpUrl).build();
        Response response = callClient(request);
        String result = fromJson(response);
        response.close();
        return result;
    }

    protected Response callClient(Request request) {

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response;
            }
            throw new DependencyError("DEPENDENCY ERROR");
        } catch (IOException e) {
            throw new DependencyError(e.getMessage());
        }
    }

    private String fromJson(Response response) {

        try {
            return OBJECT_MAPPER
                    .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                    .readValue(response.body().string(), String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("unable to deserialize object.");
        } catch (IOException e) {
            throw new RuntimeException("unable to deserialize object.");
        }

    }

}
