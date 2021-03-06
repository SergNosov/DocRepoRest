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

import java.util.Arrays;

@Slf4j
@ControllerAdvice
@PropertySource("classpath:application.properties")
public class RestExceptionHandler {

    private String propertiesMaxSize;

    @Autowired
    public void setPropertiesMaxSize(@Value("${spring.servlet.multipart.max-file-size}") String maxFileSize) {
        this.propertiesMaxSize = maxFileSize;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonMessage> handleAllException(Exception ex) {
        logginException(ex);

        final StringBuilder message = new StringBuilder("---- ")
                .append(ex.toString())
                .append(" : ")
                .append(ex.getMessage());

        CommonMessage commonMessage = new CommonMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                message.toString()
        );
        return new ResponseEntity<CommonMessage>(commonMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonMessage> handleMaxUploadSizeException(MaxUploadSizeExceededException ex) {
        logginException(ex);

        final StringBuilder message = new StringBuilder("---- Превышен размер допустимого значения при загрузки файла: ")
                .append(propertiesMaxSize)
                .append("; ")
                .append(ex.getMessage());

        CommonMessage commonMessage = new CommonMessage(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.toString(),
                message.toString()
        );
        return new ResponseEntity<CommonMessage>(commonMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonMessage> handleBadCredentialsException(BadCredentialsException ex) {
        logginException(ex);

        final StringBuilder message = new StringBuilder(ex.toString())
                .append(" : ")
                .append(ex.getMessage());

        CommonMessage commonMessage = new CommonMessage(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.toString(),
                message.toString()
        );
        return new ResponseEntity<CommonMessage>(commonMessage, HttpStatus.UNAUTHORIZED);
    }

    private void logginException(Exception ex) {
        log.error("--- exception: " + ex.getMessage());
        Arrays.stream(ex.getStackTrace()).forEach(el -> log.error("--- stackTrace: " + el));
    }
}
