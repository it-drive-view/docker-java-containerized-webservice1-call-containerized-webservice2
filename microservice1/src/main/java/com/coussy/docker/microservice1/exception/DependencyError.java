package com.coussy.docker.microservice1.exception;

public class DependencyError extends RuntimeException {

    public DependencyError(String message) {
        super(message);
    }

}
