package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice
@PropertySource("classpath:application.properties")
public class RestExceptionHandler {

    private String propertiesMaxSize;

    @Autowired
    public void setPropertiesMaxSize (@Value("${spring.servlet.multipart.max-file-size}") String maxFileSize){
        this.propertiesMaxSize = maxFileSize;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonMessage> handleAllException(Exception ex) {

       // ex.printStackTrace();
        log.error("--- exception: "+ex.getMessage());
        CommonMessage commonMessage = new CommonMessage("---- " + ex.toString() + " : " + ex.getMessage());
        return new ResponseEntity<CommonMessage>(commonMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonMessage> handleMaxUploadSizeException(MaxUploadSizeExceededException ex) {

        log.error("--- exception: "+ex.getMessage());
        CommonMessage commonMessage = new CommonMessage("---- Превышен размер допустимого значения при загрузки файла: " +
                propertiesMaxSize + "; " + ex.getMessage());
        return new ResponseEntity<CommonMessage>(commonMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonMessage> handleBadCredentialsException(BadCredentialsException ex) {

      //  ex.printStackTrace();
        log.error("--- exception: "+ex.getMessage());
        CommonMessage commonMessage = new CommonMessage(ex.toString() + " : " + ex.getMessage());
        return new ResponseEntity<CommonMessage>(commonMessage, HttpStatus.UNAUTHORIZED);
    }
}
