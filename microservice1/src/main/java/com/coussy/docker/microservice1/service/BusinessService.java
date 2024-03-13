package com.coussy.docker.microservice1.service;

import com.coussy.docker.microservice1.http.MicroService2HttpClient;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    private final MicroService2HttpClient httpClient;

    public BusinessService(MicroService2HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String callMicroservice2() {
        return "From microservice1 : expression returned from microservice2 \n " +
                httpClient.callMicroService2() + "\n";
    }

}
