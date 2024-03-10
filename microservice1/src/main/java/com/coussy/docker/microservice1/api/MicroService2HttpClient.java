package com.coussy.docker.microservice1.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

    public MicroService2HttpClient(OkHttpClient okHttpClient, String url ) {
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
        String result = fromJson(response, new TypeReference<String>() {});
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

    private <T> T fromJson(Response response, TypeReference<T> valueTyperRef)  {

        try {
            return OBJECT_MAPPER
                    .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                    .readValue(response.body().toString(), valueTyperRef);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("unable to deserialize object");
        }
    }

}
