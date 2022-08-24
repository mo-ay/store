package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ResourcesNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ResourcesNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleResourcesNotFoundException(ResourcesNotFound ex) {
        return ex.getMessage();
    }
}
