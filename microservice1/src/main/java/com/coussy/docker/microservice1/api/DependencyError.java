package com.coussy.docker.microservice1.api;

public class DependencyError extends RuntimeException {

    public DependencyError(String message) {
        super(message);
    }

}
