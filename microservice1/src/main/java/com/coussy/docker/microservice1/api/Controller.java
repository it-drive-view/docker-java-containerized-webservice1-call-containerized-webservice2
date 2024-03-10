package com.coussy.docker.microservice1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class Controller {


    @Autowired
    MicroService2HttpClient microService2HttpClient;

    @GetMapping("/test")
    public String test() {
        return "service microservice1 available.";
    }

    @GetMapping("/bean")
    public String bean() {
//        return microService2HttpClient != null ? "exist" : "null";
        return microService2HttpClient.getUrl();
    }

    @GetMapping("/call/microservice2")
    public String microservice2() {
        return microService2HttpClient.callMicroService2();
    }

}



