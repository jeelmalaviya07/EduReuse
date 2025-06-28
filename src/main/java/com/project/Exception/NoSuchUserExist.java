package com.project.Exception;

public class NoSuchUserExist extends RuntimeException{

    public NoSuchUserExist(String message) {
        super(message);
    }
}
