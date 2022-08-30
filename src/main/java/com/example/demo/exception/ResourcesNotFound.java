package com.example.demo.exception;

import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND)
public class ResourcesNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourcesNotFound(String message) {
        super(message);
    }
}

