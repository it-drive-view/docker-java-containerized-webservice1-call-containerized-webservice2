package com.coussy.docker.microservice1.api;

import com.coussy.docker.microservice1.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class Controller {

    @Autowired
    BusinessService businessService;

    @GetMapping("/test")
    public String test() {
        return "service microservice1 available.";
    }

    @GetMapping("/call/microservice2")
    public String microservice2() {
        return businessService.callMicroservice2();
    }

}



