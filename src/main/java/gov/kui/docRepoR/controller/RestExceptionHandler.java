package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.Entity.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<CommonResponse> handleException(Exception ex){
        CommonResponse commonResponse = new CommonResponse(ex.getMessage());
        return new ResponseEntity<CommonResponse>(commonResponse, HttpStatus.BAD_REQUEST);
    }
}
