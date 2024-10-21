package com.example.cricketApplication.exceptions;

public class OfficialNotFoundException extends RuntimeException {
    public OfficialNotFoundException(String message) {
        super(message);
    }
}