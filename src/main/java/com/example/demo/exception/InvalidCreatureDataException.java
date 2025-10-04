package com.example.demo.exception;

import java.lang.RuntimeException;

public class InvalidCreatureDataException extends RuntimeException {
    public InvalidCreatureDataException(String message) {
        super(message);
    }
}
