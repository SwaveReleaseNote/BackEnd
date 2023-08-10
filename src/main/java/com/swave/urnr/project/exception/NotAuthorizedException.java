package com.swave.urnr.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends Exception{
    private static final String message = "인가되지 않은 사용자입니다.";
    public NotAuthorizedException(String message){
        super(message);
    }
}
/*
package com.example.demo.survey.exception;

public class InvalidTokenException extends Exception{
    private static final String MESSAGE = "인가되지 않은 사용자입니다";
    public InvalidTokenException() {
        super(MESSAGE);
    }
}*/
