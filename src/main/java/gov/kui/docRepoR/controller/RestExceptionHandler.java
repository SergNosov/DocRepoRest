package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.model.CommonMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonMessage> handleAllException(Exception ex){
      //  ex.printStackTrace();
        CommonMessage commonMessage = new CommonMessage("---- "+ex.toString()+ " : "+ex.getMessage());
        return new ResponseEntity<CommonMessage>(commonMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonMessage> handleBadCredentialsException(BadCredentialsException ex){
        CommonMessage commonMessage = new CommonMessage(ex.toString()+ " : "+ex.getMessage());
        return new ResponseEntity<CommonMessage>(commonMessage, HttpStatus.UNAUTHORIZED);
    }
}
